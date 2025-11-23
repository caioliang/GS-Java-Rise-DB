package br.com.fiap.rise.controller;

import br.com.fiap.rise.dto.InsightDTO;
import br.com.fiap.rise.dto.ResumeDTO;
import br.com.fiap.rise.service.InsightMessageProducer;
import br.com.fiap.rise.service.InsightService;
import br.com.fiap.rise.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/insights")
public class InsightController {

    private final ResumeService resumeService;
    private final InsightService insightService;
    private final InsightMessageProducer producer;

    public InsightController(ResumeService resumeService, InsightService insightService, InsightMessageProducer producer) {
        this.resumeService = resumeService;
        this.insightService = insightService;
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<Void> triggerGeneration(@PathVariable UUID userId) {
        ResumeDTO resume = resumeService.findByUserId(userId);
        producer.sendInightGenerationRequest(resume.getId());
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<InsightDTO> getInsights(@PathVariable UUID userId) {
        ResumeDTO resume = resumeService.findByUserId(userId);

        InsightDTO cachedResp = insightService.getLastCachedForResume(resume);

        if (cachedResp != null) {
            return ResponseEntity.ok(cachedResp);
        }

        return ResponseEntity.noContent().build();
    }
}

