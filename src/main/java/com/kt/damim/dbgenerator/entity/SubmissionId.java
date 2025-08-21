package com.kt.damim.dbgenerator.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Submission 엔티티의 복합 키 클래스
 */
public class SubmissionId implements Serializable {
    private Integer examId;
    private Integer userId;

    public SubmissionId() {}

    public SubmissionId(Integer examId, Integer userId) {
        this.examId = examId;
        this.userId = userId;
    }

    public Integer getExamId() { return examId; }
    public void setExamId(Integer examId) { this.examId = examId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmissionId that = (SubmissionId) o;
        return Objects.equals(examId, that.examId) && Objects.equals(userId, that.userId);
    }
    @Override public int hashCode() { return Objects.hash(examId, userId); }
}
