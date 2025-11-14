package br.com.fiap.rise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDTO {

    @NotBlank
    private String objective;

    @NotNull
    private UUID userId;

    private List<WorkExperienceDTO> workExperiences;
    private List<EducationalExperienceDTO> educationalExperiences;
}
