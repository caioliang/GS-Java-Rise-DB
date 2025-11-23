package br.com.fiap.rise.repository;

import br.com.fiap.rise.model.EducationalExperience;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface EducationalExperienceRepository extends JpaRepository<EducationalExperience, UUID> {
    Page<EducationalExperience> findByResumeId(UUID resumeId, Pageable pageable);
}
