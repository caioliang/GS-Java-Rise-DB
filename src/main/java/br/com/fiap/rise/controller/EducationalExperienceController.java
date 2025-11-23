package br.com.fiap.rise.controller;

import br.com.fiap.rise.dto.EducationalExperienceDTO;
import br.com.fiap.rise.service.EducationalExperienceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resumes/{resumeId}/educational-experiences")
public class EducationalExperienceController {

    private final EducationalExperienceService educationalExperienceService;

    public EducationalExperienceController(EducationalExperienceService educationalExperienceService) {
        this.educationalExperienceService = educationalExperienceService;
    }

    @GetMapping
    public ResponseEntity<Page<EducationalExperienceDTO>> findAllByResumeId(@PathVariable UUID resumeId, Pageable pageable) {
        return ResponseEntity.ok(educationalExperienceService.findByResumeId(resumeId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationalExperienceDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(educationalExperienceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EducationalExperienceDTO> create(@PathVariable UUID resumeId, @RequestBody @Valid EducationalExperienceDTO dto) {
        dto.setResumeId(resumeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(educationalExperienceService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationalExperienceDTO> update(@PathVariable UUID id, @RequestBody @Valid EducationalExperienceDTO dto) {
        return ResponseEntity.ok(educationalExperienceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        educationalExperienceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
