package br.com.fiap.rise.service;

import br.com.fiap.rise.dto.WorkExperienceDTO;
import br.com.fiap.rise.exception.ResourceNotFoundException;
import br.com.fiap.rise.model.Resume;
import br.com.fiap.rise.model.User;
import br.com.fiap.rise.model.WorkExperience;
import br.com.fiap.rise.repository.ResumeRepository;
import br.com.fiap.rise.repository.WorkExperienceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final ResumeRepository resumeRepository;

    public WorkExperienceService(WorkExperienceRepository workExperienceRepository, ResumeRepository resumeRepository) {
        this.workExperienceRepository = workExperienceRepository;
        this.resumeRepository = resumeRepository;
    }

    public WorkExperienceDTO findById(UUID id) {
        WorkExperience workExperience = workExperienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiência de trabalho não encontrada."));

        return convertToDTO(workExperience);
    }

    public List<WorkExperienceDTO> findByResumeId(UUID resumeId) {
        return workExperienceRepository.findByResumeId(resumeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public WorkExperienceDTO create(WorkExperienceDTO dto) {
        Resume resume = resumeRepository.findById(dto.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Currículo não encontrado."));

        WorkExperience savedExperience = workExperienceRepository.save(convertToEntity(dto, resume));
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
        return convertToDTO(updatedExperience);
    }

    public void delete(UUID id) {
        WorkExperience existingWorkExperience = workExperienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiência de trabalho não encontrada para exclusão."));

        workExperienceRepository.delete(existingWorkExperience);
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
