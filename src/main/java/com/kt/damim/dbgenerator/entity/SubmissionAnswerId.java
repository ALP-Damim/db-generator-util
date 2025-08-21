package com.kt.damim.dbgenerator.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * SubmissionAnswer 엔티티의 복합 키 클래스
 */
public class SubmissionAnswerId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer examId;
    private Integer userId;
    private Integer questionId;

    public SubmissionAnswerId() {}

    public SubmissionAnswerId(Integer examId, Integer userId, Integer questionId) {
        this.examId = examId;
        this.userId = userId;
        this.questionId = questionId;
    }

    public Integer getExamId() { return examId; }
    public void setExamId(Integer examId) { this.examId = examId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmissionAnswerId that = (SubmissionAnswerId) o;
        return Objects.equals(examId, that.examId) && 
               Objects.equals(userId, that.userId) && 
               Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(examId, userId, questionId);
    }
}
