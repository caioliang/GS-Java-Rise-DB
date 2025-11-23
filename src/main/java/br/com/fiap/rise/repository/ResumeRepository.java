package br.com.fiap.rise.repository;

import br.com.fiap.rise.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    Optional<Resume> findByUserId(UUID userId);

    @Query("select distinct r from Resume r left join fetch r.workExperiences we where r.id = :id")
    Optional<Resume> findByIdWithCollections(@Param("id") UUID id);
}
