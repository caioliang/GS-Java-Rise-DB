package br.com.fiap.rise.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatAIService {

    private final ChatClient chatClient;
    private final Map<UUID, ChatMemory> userChatMemories = new ConcurrentHashMap<>();

    private final String systemMessage =
            """
            Você é o 'Rise Suporte', um assistente virtual especialista no aplicativo Rise, focado em currículos, experiências, formação e insights de carreira.
            Suas principais funções são:
            1. Responder a dúvidas sobre como cadastrar um currículo, adicionar experiências de trabalho e formação.
            2. Explicar o que são os 'Insights de Carreira' e como eles são gerados (usando IA para analisar o currículo).
            3. Dar conselhos gerais de desenvolvimento profissional, soft skills e melhorias no currículo.
            4. Manter um tom de voz profissional, encorajador e útil.
            Sempre responda em português e utilize emojis relevantes.
            """;

    public ChatAIService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem(systemMessage)
                .build();
    }

    private ChatMemory getOrCreateMemory(UUID sessionId) {
        return userChatMemories.computeIfAbsent(sessionId, id ->
                MessageWindowChatMemory.builder().maxMessages(20).build()
        );
    }

    public String sendMessage(UUID userId, String message) {
        ChatMemory memory = getOrCreateMemory(userId);

        var memoryAdvisor = MessageChatMemoryAdvisor.builder(memory).build();

        return chatClient.prompt()
                .advisors(List.of(memoryAdvisor, new SimpleLoggerAdvisor()))
                .system(systemMessage)
                .user(message)
                .call()
                .content();
    }
}