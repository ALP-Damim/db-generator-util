package com.kt.damim.dbgenerator.repository;

import com.kt.damim.dbgenerator.entity.Submission;
import com.kt.damim.dbgenerator.entity.SubmissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SubmissionRepository extends JpaRepository<Submission, SubmissionId> {
    List<Submission> findByExamId(Integer examId);
    Optional<Submission> findByExamIdAndUserId(Integer examId, Integer userId);
}
