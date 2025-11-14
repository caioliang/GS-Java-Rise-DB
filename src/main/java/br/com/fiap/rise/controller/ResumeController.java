package br.com.fiap.rise.controller;

import br.com.fiap.rise.dto.ResumeDTO;
import br.com.fiap.rise.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping
    public ResponseEntity<ResumeDTO> findByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(resumeService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<ResumeDTO> save(@PathVariable UUID userId, @RequestBody @Valid ResumeDTO dto) {
        dto.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.create(dto));
    }

    @PutMapping
    public ResponseEntity<ResumeDTO> update(@PathVariable UUID userId, @RequestBody @Valid ResumeDTO dto) {
        dto.setUserId(userId);
        return ResponseEntity.ok(resumeService.update(userId, dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        resumeService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
