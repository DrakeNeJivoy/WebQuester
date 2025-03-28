package com.example.WebQuest.repository;

import com.example.WebQuest.model.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
    List<AnswerOption> findByQuestionId(Long questionId);
}

