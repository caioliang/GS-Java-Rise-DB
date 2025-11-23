package br.com.fiap.rise.WorkExperience;

import br.com.fiap.rise.model.WorkExperience;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkExperienceTest {

    @Test
    void shouldCreateWorkExperienceWithValidData() {
        WorkExperience work = new WorkExperience();
        work.setId(UUID.randomUUID());
        work.setCompany("Empresa Teste");
        work.setRole("Desenvolvedor");
        work.setStartDate(LocalDate.of(2021, 2, 1));
        work.setEndDate(LocalDate.of(2024, 1, 31));
        work.setDescription("Descrição da experiência");

        assertThat(work.getCompany()).isEqualTo("Empresa Teste");
        assertThat(work.getRole()).isEqualTo("Desenvolvedor");
        assertThat(work.getStartDate()).isEqualTo(LocalDate.of(2021, 2, 1));
        assertThat(work.getEndDate()).isEqualTo(LocalDate.of(2024, 1, 31));
        assertThat(work.getDescription()).isEqualTo("Descrição da experiência");
        assertThat(work.getId()).isNotNull();
    }
}