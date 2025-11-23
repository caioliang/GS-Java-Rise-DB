package br.com.fiap.rise.repository;

import br.com.fiap.rise.model.Insight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InsightRepository extends JpaRepository<Insight, UUID> {
    java.util.Optional<Insight> findFirstByResumeIdOrderByCreatedAtDesc(UUID resumeId);
}
