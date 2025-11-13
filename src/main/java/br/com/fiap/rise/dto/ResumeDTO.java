package br.com.fiap.rise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDTO {

    @NotBlank(message = "Objective is required.")
    private String objective;

    @NotNull(message = "User ID is required to link the resume.")
    private UUID userId;

    private List<WorkExperienceDTO> workExperiences;
    private List<EducationalExperienceDTO> educationalExperiences;
}
