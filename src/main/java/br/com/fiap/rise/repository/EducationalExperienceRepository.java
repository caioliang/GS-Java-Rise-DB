package br.com.fiap.rise.repository;

import br.com.fiap.rise.model.EducationalExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EducationalExperienceRepository extends JpaRepository<EducationalExperience, UUID> {
    List<EducationalExperience> findByResumeId(UUID resumeId);
}
