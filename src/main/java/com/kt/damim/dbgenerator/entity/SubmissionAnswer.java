package com.kt.damim.dbgenerator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "submission_answers")
@IdClass(SubmissionAnswerId.class)
@Getter @Setter @NoArgsConstructor
public class SubmissionAnswer {
    @Id
    @Column(name = "exam_id")
    private Integer examId;

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    @Column(name = "score", nullable = false)
    private BigDecimal score = BigDecimal.ZERO;

    @Column(name = "solving_time")
    private Integer solvingTime;
}