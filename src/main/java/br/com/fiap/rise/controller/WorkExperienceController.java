package br.com.fiap.rise.controller;

import br.com.fiap.rise.dto.WorkExperienceDTO;
import br.com.fiap.rise.service.WorkExperienceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resumes/{resumeId}/work-experiences")
public class WorkExperienceController {
    private final WorkExperienceService workExperienceService;

    public WorkExperienceController(WorkExperienceService workExperienceService) {
        this.workExperienceService = workExperienceService;
    }

    @GetMapping
    public ResponseEntity<List<WorkExperienceDTO>> findAllByResumeId(@PathVariable UUID resumeId) {
        List<WorkExperienceDTO> workExperiences = workExperienceService.findByResumeId(resumeId);
        return ResponseEntity.ok(workExperiences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkExperienceDTO> findById(@PathVariable UUID id) {
        WorkExperienceDTO workExperience = workExperienceService.findById(id);
        return ResponseEntity.ok(workExperience);
    }

    @PostMapping
    public ResponseEntity<WorkExperienceDTO> create(@PathVariable UUID resumeId, @RequestBody @Valid WorkExperienceDTO dto) {
        dto.setResumeId(resumeId);
        WorkExperienceDTO createdWorkExperience = workExperienceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkExperience);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkExperienceDTO> update(@PathVariable UUID id, @RequestBody @Valid WorkExperienceDTO dto) {
        WorkExperienceDTO updatedWorkExperience = workExperienceService.update(id, dto);
        return ResponseEntity.ok(updatedWorkExperience);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        workExperienceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
