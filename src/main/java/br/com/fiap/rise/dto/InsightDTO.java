package br.com.fiap.rise.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsightDTO {

    @JsonProperty("competencias_usuario")
    private List<String> userSkills;

    @JsonProperty("competencias_futuro")
    private List<String> futureSkills;

    @JsonProperty("trilhas_aprendizagem")
    private List<LearningPath> learningPaths;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LearningPath {
        @JsonProperty("nome")
        private String name;

        @JsonProperty("descricao")
        private String description;

        @JsonProperty("plataformas")
        private List<String> platforms;

        @JsonProperty("links")
        private List<String> links;
    }
}
