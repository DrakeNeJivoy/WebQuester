package com.example.WebQuest.service;

import com.example.WebQuest.model.SurveySubmission;
import com.example.WebQuest.repository.SurveySubmissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
            // При необходимости можно явно загрузить список responses
            // Hibernate.initialize(submission.getResponses());
            return submission;
        }
        return null;
    }
}