package br.com.fiap.rise.service;

import br.com.fiap.rise.dto.ResumeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InsightMessageConsumer {
    private final InsightService insightService;
    private final ResumeService resumeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${rise.rabbitmq.queue.insight}")
    private String insightQueueName;

    public InsightMessageConsumer(InsightService insightService, ResumeService resumeService) {
        this.insightService = insightService;
        this.resumeService = resumeService;
    }

    @RabbitListener(queues = "${rise.rabbitmq.queue.insight}")
    public void receiveMessage(String message) {
        try {
            UUID resumeId = UUID.fromString(objectMapper.readTree(message).get("resumeId").asText());
            ResumeDTO resume = resumeService.findByResumeIdOnly(resumeId);
            insightService.generateInsightsSync(resume);
        } catch (Exception e) { }
    }
}
