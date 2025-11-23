package br.com.fiap.rise.controller;

import br.com.fiap.rise.dto.ChatAIDTO;
import br.com.fiap.rise.model.Auth;
import br.com.fiap.rise.service.ChatAIService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatAIController {

    private final ChatAIService chatAIService;

    public ChatAIController(ChatAIService chatAIService) {
        this.chatAIService = chatAIService;
    }

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody @Valid ChatAIDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }

        UUID userId;
        try {
            Auth auth = (Auth) authentication.getPrincipal();
            userId = auth.getId();
        } catch (ClassCastException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro de segurança: Objeto de autenticação inválido.");
        }

        String response = chatAIService.sendMessage(userId, request.getMessage());
        return ResponseEntity.ok(response);
    }
}