package com.example.WebQuest.repository;

import com.example.WebQuest.model.SurveySubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveySubmissionRepository extends JpaRepository<SurveySubmission, Long> {
}