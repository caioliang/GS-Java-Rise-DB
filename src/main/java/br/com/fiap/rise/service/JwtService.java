package br.com.fiap.rise.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JwtService {
    private final String SECRET_KEY = "CHAVE_SECRETA_RISE";
    private final long EXPIRATION_SECONDS = 3600;

    public String generateToken(UUID userId) {
        return "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ" + userId.toString() + "Iiwicm9sZSI6InVzZXIifQ.TOKEN_SIMULADO_ASSINADO";
    }

    public String validateAndExtractUserId(String token) {
        if (!token.contains("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")) {
            throw new RuntimeException("Token JWT inválido ou expirado.");
        }
        return UUID.randomUUID().toString();
    }
}
