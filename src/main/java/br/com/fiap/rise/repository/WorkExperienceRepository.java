package br.com.fiap.rise.repository;

import br.com.fiap.rise.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, UUID> {
    Optional<WorkExperience> findById(UUID id);
    List<WorkExperience> findByResumeId(UUID resumeId);
}
