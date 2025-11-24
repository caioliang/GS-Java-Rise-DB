package br.com.fiap.rise.service;

import br.com.fiap.rise.dto.EducationalExperienceDTO;
import br.com.fiap.rise.exception.ResourceNotFoundException;
import br.com.fiap.rise.model.EducationalExperience;
import br.com.fiap.rise.model.Resume;
import br.com.fiap.rise.repository.EducationalExperienceRepository;
import br.com.fiap.rise.repository.ResumeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EducationalExperienceService {
    private final EducationalExperienceRepository educationalExperienceRepository;
    private final ResumeRepository resumeRepository;
    private final CacheManager cacheManager;

    public EducationalExperienceService(EducationalExperienceRepository educationalExperienceRepository, ResumeRepository resumeRepository, CacheManager cacheManager) {
        this.educationalExperienceRepository = educationalExperienceRepository;
        this.resumeRepository = resumeRepository;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "eduExpList", key = "#resumeId")
    public Page<EducationalExperienceDTO> findByResumeId(UUID resumeId, Pageable pageable) {
        return educationalExperienceRepository.findByResumeId(resumeId, pageable)
                .map(this::convertToDTO);
    }

    public EducationalExperienceDTO findById(UUID id) {
        EducationalExperience educationalExperience = educationalExperienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiência acadêmica não encontrada."));
        return convertToDTO(educationalExperience);
    }

    @CacheEvict(value = "eduExpList", key = "#dto.resumeId")
    public EducationalExperienceDTO create(EducationalExperienceDTO dto) {
        Resume resume = resumeRepository.findById(dto.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Currículo não encontrado."));

        EducationalExperience educationalExperience = convertToEntity(dto, resume);
        EducationalExperience savedEducationalExperience = educationalExperienceRepository.save(educationalExperience);
        try {
            if (resume.getUser() != null && cacheManager.getCache("resumes") != null) {
                cacheManager.getCache("resumes").evict(resume.getUser().getId());
            }
        } catch (Exception ex) { }

        return convertToDTO(savedEducationalExperience);
    }

    public EducationalExperienceDTO update(UUID id, EducationalExperienceDTO dto) {
        EducationalExperience existingEducationalExperience = educationalExperienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiência acadêmica não encontrada para atualização."));

        existingEducationalExperience.setInstitution(dto.getInstitution());
        existingEducationalExperience.setCourse(dto.getCourse());
        existingEducationalExperience.setStartDate(dto.getStartDate());
        existingEducationalExperience.setEndDate(dto.getEndDate());
        existingEducationalExperience.setDescription(dto.getDescription());

        EducationalExperience updatedEducationalExperience = educationalExperienceRepository.save(existingEducationalExperience);
        try {
            if (existingEducationalExperience.getResume() != null && existingEducationalExperience.getResume().getUser() != null && cacheManager.getCache("resumes") != null) {
                cacheManager.getCache("resumes").evict(existingEducationalExperience.getResume().getUser().getId());
            }
        } catch (Exception ex) { }

        return convertToDTO(updatedEducationalExperience);
    }

    @CacheEvict(value = "eduExpList", allEntries = true)
    public void delete(UUID id) {
        EducationalExperience existingEducationalExperience = educationalExperienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiência acadêmica não encontrada para exclusão."));

        educationalExperienceRepository.delete(existingEducationalExperience);
        try {
            if (existingEducationalExperience.getResume() != null && existingEducationalExperience.getResume().getUser() != null && cacheManager.getCache("resumes") != null) {
                cacheManager.getCache("resumes").evict(existingEducationalExperience.getResume().getUser().getId());
            }
        } catch (Exception ex) { }
    }

    private EducationalExperience convertToEntity(EducationalExperienceDTO dto, Resume resume) {
        EducationalExperience experience = new EducationalExperience();
        experience.setInstitution(dto.getInstitution());
        experience.setCourse(dto.getCourse());
        experience.setStartDate(dto.getStartDate());
        experience.setEndDate(dto.getEndDate());
        experience.setDescription(dto.getDescription());
        experience.setResume(resume);

        return experience;
    }

    private EducationalExperienceDTO convertToDTO(EducationalExperience entity) {
        EducationalExperienceDTO dto = new EducationalExperienceDTO();
        dto.setId(entity.getId());
        dto.setResumeId(entity.getResume() != null ? entity.getResume().getId() : null);
        dto.setInstitution(entity.getInstitution());
        dto.setCourse(entity.getCourse());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}
