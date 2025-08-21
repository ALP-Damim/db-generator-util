package com.kt.damim.dbgenerator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "submissions")
@IdClass(SubmissionId.class)
@Getter @Setter @NoArgsConstructor
public class Submission {
    @Id @Column(name = "exam_id")
    private Integer examId;

    @Id @Column(name = "user_id")
    private Integer userId;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt = Instant.now();

    @Column(name = "total_score", nullable = false)
    private BigDecimal totalScore = BigDecimal.ZERO;

    @Column(name = "feedback")
    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_status", nullable = false)
    private FeedbackStatus feedbackStatus = FeedbackStatus.NONE;

    @Column(name = "feedback_requested_at")
    private Instant feedbackRequestedAt;

    @Column(name = "feedback_retry_count", nullable = false)
    private Integer feedbackRetryCount = 0;

    public enum FeedbackStatus {
        NONE,
        REQUESTED,
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }
}
