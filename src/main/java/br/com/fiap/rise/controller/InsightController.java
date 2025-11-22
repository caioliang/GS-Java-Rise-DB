package br.com.fiap.rise.controller;

import br.com.fiap.rise.dto.InsightDTO;
import br.com.fiap.rise.dto.ResumeDTO;
import br.com.fiap.rise.service.AiInsightService;
import br.com.fiap.rise.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/insights")
public class InsightController {

    private final ResumeService resumeService;
    private final AiInsightService aiInsightService;

    public InsightController(ResumeService resumeService, AiInsightService aiInsightService) {
        this.resumeService = resumeService;
        this.aiInsightService = aiInsightService;
    }

    @GetMapping
    public ResponseEntity<InsightDTO> getInsights(@PathVariable UUID userId, @RequestParam(required = false, defaultValue = "false") boolean cached) {
        ResumeDTO resume = resumeService.findByUserId(userId);
        if (cached) {
            InsightDTO cachedResp = aiInsightService.getLastCachedForResume(resume);
            if (cachedResp != null) return ResponseEntity.ok(cachedResp);
        }
        return ResponseEntity.ok(aiInsightService.generateInsights(resume));
    }
}

