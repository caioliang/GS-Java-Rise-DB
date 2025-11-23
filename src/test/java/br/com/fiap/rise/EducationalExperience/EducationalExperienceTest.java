package br.com.fiap.rise.EducationalExperience;

import br.com.fiap.rise.model.EducationalExperience;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EducationalExperienceTest {

    @Test
    void shouldCreateEducationalExperienceWithValidData() {
        EducationalExperience edu = new EducationalExperience();
        edu.setId(UUID.randomUUID());
        edu.setInstitution("Universidade de Teste");
        edu.setCourse("Engenharia de Software");
        edu.setStartDate(LocalDate.of(2020, 1, 1));
        edu.setEndDate(LocalDate.of(2023, 12, 31));
        edu.setDescription("Descrição do curso");

        assertThat(edu.getInstitution()).isEqualTo("Universidade de Teste");
        assertThat(edu.getCourse()).isEqualTo("Engenharia de Software");
        assertThat(edu.getStartDate()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(edu.getEndDate()).isEqualTo(LocalDate.of(2023, 12, 31));
        assertThat(edu.getDescription()).isEqualTo("Descrição do curso");
        assertThat(edu.getId()).isNotNull();
    }
}