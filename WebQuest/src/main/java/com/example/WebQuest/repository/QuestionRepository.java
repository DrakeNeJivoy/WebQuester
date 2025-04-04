package com.example.WebQuest.repository;

import com.example.WebQuest.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurveyId(Long surveyId);

    @Transactional // Добавляем аннотацию
    void deleteBySurveyId(Long surveyId); // Добавляем метод
}

