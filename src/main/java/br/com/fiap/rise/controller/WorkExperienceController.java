package br.com.fiap.rise.controller;

import br.com.fiap.rise.dto.WorkExperienceDTO;
import br.com.fiap.rise.service.WorkExperienceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/resumes/{resumeId}/work-experiences")
public class WorkExperienceController {
    private static final Logger log = LoggerFactory.getLogger(WorkExperienceController.class);
    private final WorkExperienceService workExperienceService;

    public WorkExperienceController(WorkExperienceService workExperienceService) {
        this.workExperienceService = workExperienceService;
    }

    @GetMapping
    public ResponseEntity<List<WorkExperienceDTO>> findAllByResumeId(@PathVariable UUID resumeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("WorkExperienceController.findAllByResumeId: auth={} principal={} authorities={}", auth, auth != null ? auth.getPrincipal() : null, auth != null ? auth.getAuthorities() : null);
        return ResponseEntity.ok(workExperienceService.findByResumeId(resumeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkExperienceDTO> findById(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("WorkExperienceController.findById: auth={} principal={} authorities={}", auth, auth != null ? auth.getPrincipal() : null, auth != null ? auth.getAuthorities() : null);
        return ResponseEntity.ok(workExperienceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<WorkExperienceDTO> create(@PathVariable UUID resumeId, @RequestBody @Valid WorkExperienceDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("WorkExperienceController.create: auth={} principal={} authorities={}", auth, auth != null ? auth.getPrincipal() : null, auth != null ? auth.getAuthorities() : null);
        dto.setResumeId(resumeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(workExperienceService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkExperienceDTO> update(@PathVariable UUID id, @RequestBody @Valid WorkExperienceDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("WorkExperienceController.update: auth={} principal={} authorities={}", auth, auth != null ? auth.getPrincipal() : null, auth != null ? auth.getAuthorities() : null);
        return ResponseEntity.ok(workExperienceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID resumeId, @PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("WorkExperienceController.delete: resumeId={} id={} auth={} principal={} authorities={}", resumeId, id, auth, auth != null ? auth.getPrincipal() : null, auth != null ? auth.getAuthorities() : null);
        workExperienceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
