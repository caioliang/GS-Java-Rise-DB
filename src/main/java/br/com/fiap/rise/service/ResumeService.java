package br.com.fiap.rise.service;

import br.com.fiap.rise.dto.ResumeDTO;
import br.com.fiap.rise.model.Resume;
import br.com.fiap.rise.repository.ResumeRepository;
import br.com.fiap.rise.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public ResumeDTO findByUserId(UUID userId) {
        Resume resume = resumeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Currículo não encontrado para o usuário."));

        return convertToDTO(resume);
    }
}
