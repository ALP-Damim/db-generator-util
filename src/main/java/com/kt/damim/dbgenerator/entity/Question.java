package com.kt.damim.dbgenerator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Table(name = "questions")
@Getter @Setter @NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(name = "exam_id")
    private Integer examId;

    @Enumerated(EnumType.STRING)
    @Column(name = "qtype", nullable = false)
    private QuestionType qtype;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "choices", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String choices;

    @Column(name = "answer_key")
    private String answerKey;

    @Column(name = "points", nullable = false)
    private BigDecimal points = BigDecimal.ZERO;

    @Column(name = "position", nullable = false)
    private int position;
}
