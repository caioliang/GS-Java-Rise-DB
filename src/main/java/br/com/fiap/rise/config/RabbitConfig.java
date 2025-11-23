package br.com.fiap.rise.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${rise.rabbitmq.queue.insight}")
    private String insightQueueName;

    @Bean
    public Queue insightQueue() {
        return new Queue(insightQueueName, true);
    }
}
