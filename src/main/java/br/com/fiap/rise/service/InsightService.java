package br.com.fiap.rise.service;

import br.com.fiap.rise.dto.InsightDTO;
import br.com.fiap.rise.dto.ResumeDTO;
import br.com.fiap.rise.model.Insight;
import br.com.fiap.rise.repository.InsightRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InsightService {

    private static final String HF_URL = "https://levmn-fiap-rise-ai.hf.space/gerar-insights";
    private final RestTemplate restTemplate = new RestTemplate();
    private final InsightRepository insightRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CacheManager cacheManager;

    public InsightService(InsightRepository insightRepository, CacheManager cacheManager) {
        this.insightRepository = insightRepository;
        this.cacheManager = cacheManager;
    }

    public InsightDTO generateInsightsSync(ResumeDTO resume) {
        Map<String, String> body = new HashMap<>();

        String experiences = buildExperiences(resume);
        String education = buildEducation(resume);
        String objective = resume.getObjective() != null ? resume.getObjective() : "";

        body.put("experiencias", experiences);
        body.put("formacao", education);
        body.put("objetivo", objective);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        String rawResponse = restTemplate.postForObject(HF_URL, request, String.class);
        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            JsonNode resultNode = root.path("resultado");
            InsightDTO dto = objectMapper.treeToValue(resultNode, InsightDTO.class);

            Insight insight = new Insight();
            insight.setResumeId(resume.getId());
            try {
                insight.setPayload(resultNode.toString());
            } catch (Exception ex) {
                insight.setPayload(rawResponse);
            }
            try {
                java.util.Optional<Insight> existing = insightRepository.findFirstByResumeIdOrderByCreatedAtDesc(resume.getId());
                if (existing.isPresent()) {
                    insight.setId(existing.get().getId());
                }
            } catch (Exception ex) {
            }
            insightRepository.save(insight);
            try {
                if (resume != null) {
                    if (cacheManager.getCache("insights") != null && resume.getId() != null) {
                        cacheManager.getCache("insights").evict(resume.getId());
                    }
                    if (cacheManager.getCache("resumes") != null && resume.getUserId() != null) {
                        cacheManager.getCache("resumes").evict(resume.getUserId());
                    }
                }
            } catch (Exception ex) { }

            return dto;
        } catch (Exception e) {
            try {
                Insight insight = new Insight();
                insight.setResumeId(resume.getId());
                insight.setPayload(rawResponse);
                try {
                    java.util.Optional<Insight> existing = insightRepository.findFirstByResumeIdOrderByCreatedAtDesc(resume.getId());
                    if (existing.isPresent()) {
                        insight.setId(existing.get().getId());
                    }
                } catch (Exception ex) { }
                insightRepository.save(insight);
                try {
                    if (resume != null) {
                        if (cacheManager.getCache("insights") != null && resume.getId() != null) {
                            cacheManager.getCache("insights").evict(resume.getId());
                        }
                        if (cacheManager.getCache("resumes") != null && resume.getUserId() != null) {
                            cacheManager.getCache("resumes").evict(resume.getUserId());
                        }
                    }
                } catch (Exception ex) { }
            } catch (Exception ex) { }
            return new InsightDTO();
        }
    }

    @Cacheable(value = "insights", key = "#resume.id")
    public InsightDTO getLastCachedForResume(ResumeDTO resume) {
        if (resume.getId() == null) return null;
        return insightRepository.findFirstByResumeIdOrderByCreatedAtDesc(resume.getId())
                .map(i -> {
                    try {
                        JsonNode root = objectMapper.readTree(i.getPayload());
                        JsonNode resultNode = root.path("resultado");
                        if (resultNode.isMissingNode() || resultNode.isNull()) {
                            resultNode = root;
                        }
                        return objectMapper.treeToValue(resultNode, InsightDTO.class);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                }).orElse(null);
    }

    private String buildExperiences(ResumeDTO resume) {
        if (resume.getWorkExperiences() == null || resume.getWorkExperiences().isEmpty()) return "";
        List<String> parts = resume.getWorkExperiences().stream()
                .map(w -> {
                    String role = w.getRole() != null ? w.getRole() : "";
                    String company = w.getCompany() != null ? w.getCompany() : "";
                    return (role + " " + company).trim();
                })
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
        return String.join("; ", parts);
    }

    private String buildEducation(ResumeDTO resume) {
        if (resume.getEducationalExperiences() == null || resume.getEducationalExperiences().isEmpty()) return "";
        List<String> parts = resume.getEducationalExperiences().stream()
                .map(e -> {
                    String course = e.getCourse() != null ? e.getCourse() : "";
                    String inst = e.getInstitution() != null ? e.getInstitution() : "";
                    return (course + " " + inst).trim();
                })
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
        return String.join("; ", parts);
    }
}
