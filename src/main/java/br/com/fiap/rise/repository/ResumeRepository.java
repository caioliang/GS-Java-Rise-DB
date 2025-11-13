package br.com.fiap.rise.repository;

import br.com.fiap.rise.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    Optional<Resume> findByUserId(UUID userId);
}
