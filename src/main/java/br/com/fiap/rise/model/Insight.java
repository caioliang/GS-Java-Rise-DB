package br.com.fiap.rise.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_INSIGHTS")
public class Insight {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "resume_id")
    private UUID resumeId;

    @Column(columnDefinition = "jsonb")
    private String payload;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();
}
