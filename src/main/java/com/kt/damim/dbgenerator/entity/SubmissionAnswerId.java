package com.kt.damim.dbgenerator.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * SubmissionAnswer 엔티티의 복합 키 클래스
 */
public class SubmissionAnswerId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer submissionExamId;
    private Integer submissionUserId;
    private Integer questionId;

    public SubmissionAnswerId() {}

    public SubmissionAnswerId(Integer submissionExamId, Integer submissionUserId, Integer questionId) {
        this.submissionExamId = submissionExamId;
        this.submissionUserId = submissionUserId;
        this.questionId = questionId;
    }

    public Integer getSubmissionExamId() { return submissionExamId; }
    public void setSubmissionExamId(Integer submissionExamId) { this.submissionExamId = submissionExamId; }
    public Integer getSubmissionUserId() { return submissionUserId; }
    public void setSubmissionUserId(Integer submissionUserId) { this.submissionUserId = submissionUserId; }
    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmissionAnswerId that = (SubmissionAnswerId) o;
        return Objects.equals(submissionExamId, that.submissionExamId) && 
               Objects.equals(submissionUserId, that.submissionUserId) && 
               Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(submissionExamId, submissionUserId, questionId);
    }
}
