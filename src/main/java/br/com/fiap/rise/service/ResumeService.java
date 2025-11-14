package br.com.fiap.rise.service;

import br.com.fiap.rise.dto.EducationalExperienceDTO;
import br.com.fiap.rise.dto.ResumeDTO;
import br.com.fiap.rise.dto.WorkExperienceDTO;
import br.com.fiap.rise.model.EducationalExperience;
import br.com.fiap.rise.model.Resume;
import br.com.fiap.rise.model.User;
import br.com.fiap.rise.model.WorkExperience;
import br.com.fiap.rise.repository.ResumeRepository;
import br.com.fiap.rise.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    public ResumeDTO findByUserId(UUID userId) {
        Resume resume = resumeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Currículo não encontrado para o usuário."));

        return convertToDTO(resume);
    }

    public ResumeDTO create(ResumeDTO resumeDTO) {
        User user = userRepository.findById(resumeDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (resumeRepository.findByUserId(user.getId()).isPresent()) {
            throw new RuntimeException("Já existe um currículo para este usuário.");
        }

        Resume resume = convertToEntity(resumeDTO, user);
        return convertToDTO(resumeRepository.save(resume));
    }

    public ResumeDTO update(UUID userId, ResumeDTO dto) {
        Resume existingResume = resumeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Currículo não encontrado"));

        existingResume.setObjective(dto.getObjective());
        return convertToDTO(resumeRepository.save(existingResume));
    }

    public void delete(UUID userId) {
        Resume existingResume = resumeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Currículo não encontrado"));

        resumeRepository.delete(existingResume);
    }

    private Resume convertToEntity(ResumeDTO dto, User user) {
        Resume resume = new Resume();
        resume.setObjective(dto.getObjective());
        resume.setUser(user);

        if (dto.getWorkExperiences() != null) {
            resume.setWorkExperiences(
                    mapWorkExperiencesToEntity(dto.getWorkExperiences(), resume)
            );
        }

        if (dto.getEducationalExperiences() != null) {
            resume.setEducationalExperiences(
                    mapEducationalExperiencesToEntity(dto.getEducationalExperiences(), resume)
            );
        }

        return resume;
    }

    private List<WorkExperience> mapWorkExperiencesToEntity(List<WorkExperienceDTO> dtos, Resume resume) {
        if (dtos == null) return Collections.emptyList();

        return dtos.stream()
                .map(dto -> {
                    WorkExperience exp = new WorkExperience();
                    exp.setCompany(dto.getCompany());
                    exp.setRole(dto.getRole());
                    exp.setStartDate(dto.getStartDate());
                    exp.setEndDate(dto.getEndDate());
                    exp.setDescription(dto.getDescription());
                    exp.setResume(resume);
                    return exp;
                }).collect(Collectors.toList());
    }

    private List<EducationalExperience> mapEducationalExperiencesToEntity(List<EducationalExperienceDTO> dtos, Resume resume) {
        if (dtos == null) return Collections.emptyList();

        return dtos.stream()
                .map(dto -> {
                    EducationalExperience exp = new EducationalExperience();
                    exp.setInstitution(dto.getInstitution());
                    exp.setCourse(dto.getCourse());
                    exp.setStartDate(dto.getStartDate());
                    exp.setEndDate(dto.getEndDate());
                    exp.setDescription(dto.getDescription());
                    exp.setResume(resume);
                    return exp;
                }).collect(Collectors.toList());
    }

    private ResumeDTO convertToDTO(Resume entity) {
        ResumeDTO dto = new ResumeDTO();
        dto.setObjective(entity.getObjective());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);

        if (entity.getWorkExperiences() != null) {
            dto.setWorkExperiences(entity.getWorkExperiences().stream()
                    .map(this::mapWorkExperienceToDTO)
                    .collect(Collectors.toList()));
        }

        if (entity.getEducationalExperiences() != null) {
            dto.setEducationalExperiences(entity.getEducationalExperiences().stream()
                    .map(this::mapEducationalExperienceToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private WorkExperienceDTO mapWorkExperienceToDTO(WorkExperience entity) {
        WorkExperienceDTO dto = new WorkExperienceDTO();
        dto.setResumeId(entity.getResume() != null ? entity.getResume().getId() : null);
        dto.setCompany(entity.getCompany());
        dto.setRole(entity.getRole());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    private EducationalExperienceDTO mapEducationalExperienceToDTO(EducationalExperience entity) {
        EducationalExperienceDTO dto = new EducationalExperienceDTO();
        dto.setResumeId(entity.getResume() != null ? entity.getResume().getId() : null);
        dto.setInstitution(entity.getInstitution());
        dto.setCourse(entity.getCourse());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
