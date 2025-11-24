package br.com.fiap.rise.service;

import br.com.fiap.rise.dto.WorkExperienceDTO;
import br.com.fiap.rise.exception.ResourceNotFoundException;
import br.com.fiap.rise.model.Resume;
import br.com.fiap.rise.model.WorkExperience;
import br.com.fiap.rise.repository.ResumeRepository;
import br.com.fiap.rise.repository.WorkExperienceRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WorkExperienceService {


    private final WorkExperienceRepository workExperienceRepository;
    private final ResumeRepository resumeRepository;
    private final CacheManager cacheManager;

    public WorkExperienceService(WorkExperienceRepository workExperienceRepository, ResumeRepository resumeRepository, CacheManager cacheManager) {
        this.workExperienceRepository = workExperienceRepository;
        this.resumeRepository = resumeRepository;
        this.cacheManager = cacheManager;
    }

    public WorkExperienceDTO findById(UUID id) {
        WorkExperience workExperience = workExperienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiência de trabalho não encontrada."));

        return convertToDTO(workExperience);
    }

    @Cacheable(value = "workExpList", key = "#resumeId")
    public Page<WorkExperienceDTO> findByResumeId(UUID resumeId, Pageable pageable) {
        return workExperienceRepository.findByResumeId(resumeId, pageable)
                .map(this::convertToDTO);
    }

    @CacheEvict(value = "workExpList", key = "#dto.resumeId")
    public WorkExperienceDTO create(WorkExperienceDTO dto) {
        Resume resume = resumeRepository.findById(dto.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Currículo não encontrado."));

        WorkExperience savedExperience = workExperienceRepository.save(convertToEntity(dto, resume));
        try {
            if (resume.getUser() != null && cacheManager.getCache("resumes") != null) {
                cacheManager.getCache("resumes").evict(resume.getUser().getId());
            }
        } catch (Exception ex) { }

        return convertToDTO(savedExperience);
    }

    public WorkExperienceDTO update(UUID id, WorkExperienceDTO dto) {
        WorkExperience existingWorkExperience = workExperienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiência de trabalho não encontrada para atualização."));

        existingWorkExperience.setCompany(dto.getCompany());
        existingWorkExperience.setRole(dto.getRole());
        existingWorkExperience.setStartDate(dto.getStartDate());
        existingWorkExperience.setEndDate(dto.getEndDate());
        existingWorkExperience.setDescription(dto.getDescription());

        WorkExperience updatedExperience = workExperienceRepository.save(existingWorkExperience);
        try {
            if (existingWorkExperience.getResume() != null && existingWorkExperience.getResume().getUser() != null && cacheManager.getCache("resumes") != null) {
                cacheManager.getCache("resumes").evict(existingWorkExperience.getResume().getUser().getId());
            }
        } catch (Exception ex) { }

        return convertToDTO(updatedExperience);
    }

    @CacheEvict(value = "workExpList", allEntries = true)
    public void delete(UUID id) {
        var opt = workExperienceRepository.findById(id);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException("Experiência de trabalho não encontrada para exclusão. id=" + id);
        }

        WorkExperience existingWorkExperience = opt.get();
        workExperienceRepository.delete(existingWorkExperience);
        try {
            if (existingWorkExperience.getResume() != null && existingWorkExperience.getResume().getUser() != null && cacheManager.getCache("resumes") != null) {
                cacheManager.getCache("resumes").evict(existingWorkExperience.getResume().getUser().getId());
            }
        } catch (Exception ex) { }
    }

    private WorkExperience convertToEntity(WorkExperienceDTO dto, Resume resume) {
        WorkExperience experience = new WorkExperience();
        experience.setCompany(dto.getCompany());
        experience.setRole(dto.getRole());
        experience.setStartDate(dto.getStartDate());
        experience.setEndDate(dto.getEndDate());
        experience.setDescription(dto.getDescription());
        experience.setResume(resume);

        return experience;
    }

    private WorkExperienceDTO convertToDTO(WorkExperience entity) {
        WorkExperienceDTO dto = new WorkExperienceDTO();
        dto.setId(entity.getId());
        dto.setResumeId(entity.getResume() != null ? entity.getResume().getId() : null);
        dto.setCompany(entity.getCompany());
        dto.setRole(entity.getRole());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}
