package br.com.fiap.rise.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDTO {

    private UUID id;

    @Size(max = 200)
    private String objective;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID userId;

    private List<WorkExperienceDTO> workExperiences;
    private List<EducationalExperienceDTO> educationalExperiences;
}
