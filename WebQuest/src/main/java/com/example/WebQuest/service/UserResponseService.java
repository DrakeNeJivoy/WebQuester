package com.example.WebQuest.service;

import com.example.WebQuest.model.UserResponse;
import com.example.WebQuest.repository.UserResponseRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserResponseService {

    private final UserResponseRepository userResponseRepository;

    public UserResponseService(UserResponseRepository userResponseRepository) {
        this.userResponseRepository = userResponseRepository;
    }

    public Map<Long, Long> getAnswerOptionCountsForQuestion(Long questionId, Long surveyId) {
        List<UserResponse> responses = userResponseRepository.findByQuestionIdAndSurveyId(questionId, surveyId);
        Map<Long, Long> counts = new HashMap<>();
        for (UserResponse response : responses) {
            for (var selectedAnswer : response.getSelectedAnswers()) {
                counts.put(selectedAnswer.getId(), counts.getOrDefault(selectedAnswer.getId(), 0L) + 1);
            }
        }
        return counts;
    }

    public Long countCorrectResponsesForQuestion(Long questionId, Long surveyId) {
        return userResponseRepository.countByQuestionIdAndSurveyIdAndCorrectTrue(questionId, surveyId);
    }
}