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
        return ResponseEntity.ok(workExperienceService.findByResumeId(resumeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkExperienceDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(workExperienceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<WorkExperienceDTO> create(@PathVariable UUID resumeId, @RequestBody @Valid WorkExperienceDTO dto) {
        dto.setResumeId(resumeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(workExperienceService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkExperienceDTO> update(@PathVariable UUID id, @RequestBody @Valid WorkExperienceDTO dto) {
        return ResponseEntity.ok(workExperienceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        workExperienceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
