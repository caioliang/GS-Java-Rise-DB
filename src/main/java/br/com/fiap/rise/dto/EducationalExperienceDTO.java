package br.com.fiap.rise.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationalExperienceDTO {

    private UUID id;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID resumeId;

    @NotBlank
    @Length(min = 3, max = 100)
    private String institution;

    @NotBlank
    @Length(min = 3, max = 100)
    private String course;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotBlank
    private String description;

}
