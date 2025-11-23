package br.com.fiap.rise.repository;

import br.com.fiap.rise.model.WorkExperience;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, UUID> {
    Optional<WorkExperience> findById(UUID id);
    Page<WorkExperience> findByResumeId(UUID resumeId, Pageable pageable);
}
