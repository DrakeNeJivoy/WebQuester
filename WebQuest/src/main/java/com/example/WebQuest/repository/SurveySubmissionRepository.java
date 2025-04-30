package com.example.WebQuest.repository;

import com.example.WebQuest.model.SurveySubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveySubmissionRepository extends JpaRepository<SurveySubmission, Long> {

    long countBySurveyId(Long surveyId);

    List<SurveySubmission> findBySurveyIdOrderBySubmissionDateDesc(Long surveyId);

    List<SurveySubmission> findAllByOrderBySubmissionDateDesc();

    List<SurveySubmission> findByUserIdOrderBySubmissionDateDesc(Long userId);
}