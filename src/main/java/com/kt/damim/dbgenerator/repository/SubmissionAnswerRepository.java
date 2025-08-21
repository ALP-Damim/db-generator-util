package com.kt.damim.dbgenerator.repository;

import com.kt.damim.dbgenerator.entity.SubmissionAnswer;
import com.kt.damim.dbgenerator.entity.SubmissionAnswerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SubmissionAnswerRepository extends JpaRepository<SubmissionAnswer, SubmissionAnswerId> {
    List<SubmissionAnswer> findByExamIdAndUserId(Integer examId, Integer userId);
}
