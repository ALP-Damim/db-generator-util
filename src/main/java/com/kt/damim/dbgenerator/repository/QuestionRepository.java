package com.kt.damim.dbgenerator.repository;

import com.kt.damim.dbgenerator.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByExamIdOrderByPosition(Integer examId);
}
