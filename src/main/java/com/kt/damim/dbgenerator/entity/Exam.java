package com.kt.damim.dbgenerator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "exams")
@Getter @Setter @NoArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long id;

    @Column(name = "session_id")
    private Integer sessionId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "is_ready", nullable = false)
    private Boolean isReady = false;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
