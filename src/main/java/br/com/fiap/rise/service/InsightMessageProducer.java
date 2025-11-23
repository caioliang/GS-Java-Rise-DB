package br.com.fiap.rise.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InsightMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rise.rabbitmq.queue.insight}")
    private String insightQueueName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public InsightMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendInightGenerationRequest(UUID resumeId) {
        try {
            String message = objectMapper.writeValueAsString(new InsightRequestPayload(resumeId));
            rabbitTemplate.convertAndSend(insightQueueName, message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send insight generation request", e);
        }
    }

    @Getter
    private static class InsightRequestPayload {
        public UUID resumeId;
        public InsightRequestPayload(UUID resumeId) {
            this.resumeId = resumeId;
        }
    }
}
