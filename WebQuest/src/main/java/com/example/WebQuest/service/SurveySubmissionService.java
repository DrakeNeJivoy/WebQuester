package com.example.WebQuest.service;

import com.example.WebQuest.model.SurveySubmission;
import com.example.WebQuest.repository.SurveySubmissionRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurveySubmissionService {

    private final SurveySubmissionRepository surveySubmissionRepository;

    public SurveySubmissionService(SurveySubmissionRepository surveySubmissionRepository) {
        this.surveySubmissionRepository = surveySubmissionRepository;
    }

    @Transactional
    public SurveySubmission getSubmissionWithResponses(Long id) {
        Optional<SurveySubmission> submissionOptional = surveySubmissionRepository.findById(id);
        if (submissionOptional.isPresent()) {
            SurveySubmission submission = submissionOptional.get();
            Hibernate.initialize(submission.getResponses());
            return submission;
        }
        return null;
    }

    public long countSubmissionsForSurvey(Long surveyId) {
        return surveySubmissionRepository.countBySurveyId(surveyId);
    }

    public List<SurveySubmission> getSubmissionsBySurveyIdWithUser(Long surveyId) {
        return surveySubmissionRepository.findBySurveyIdOrderBySubmissionDateDesc(surveyId);
    }

    public List<SurveySubmission> getSubmissionsByUserIdWithSurvey(Long userId) {
        return surveySubmissionRepository.findByUserIdOrderBySubmissionDateDesc(userId);
    }
}