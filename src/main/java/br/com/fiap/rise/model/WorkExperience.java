package br.com.fiap.rise.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_WORK_EXPERIENCE")
public class WorkExperience {

    @Id
    private UUID id = UUID.randomUUID();

    @NotBlank
    private String company;

    @NotBlank
    private String role;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotBlank
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;
}
