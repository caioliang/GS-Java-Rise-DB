package br.com.fiap.rise.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_EDUCATIONAL_EXPERIENCE")
public class EducationalExperience {

    @Id
    private UUID id = UUID.randomUUID();

    @NotBlank
    @Length(min = 3, max = 100)
    private String institution;

    @NotBlank
    @Length(min = 3, max = 100)
    private String course;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @Lob
    @NotBlank
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;
}
