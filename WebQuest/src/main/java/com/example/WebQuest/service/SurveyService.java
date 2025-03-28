package com.example.WebQuest.service;

import org.springframework.transaction.annotation.Transactional;
import com.example.WebQuest.dto.*;
import com.example.WebQuest.model.*;
import com.example.WebQuest.repository.SurveyRepository;
import com.example.WebQuest.repository.QuestionRepository;
import com.example.WebQuest.repository.AnswerOptionRepository;
import com.example.WebQuest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final UserRepository userRepository; // Добавляем объявление

    public SurveyService(SurveyRepository surveyRepository,
                         QuestionRepository questionRepository,
                         AnswerOptionRepository answerOptionRepository,
                         UserRepository userRepository) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.userRepository = userRepository;
    }

    public Survey getSurveyById(Long id) {
        return surveyRepository.findById(id).orElse(null);
    }


    @Transactional
    public Survey createSurvey(SurveyRequest surveyRequest, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Survey survey = new Survey();
        survey.setTitle(surveyRequest.getTitle());
        survey.setUser(user);
        surveyRepository.save(survey);

        for (QuestionRequest questionRequest : surveyRequest.getQuestions()) {
            Question question = new Question();
            question.setText(questionRequest.getText());
            question.setSurvey(survey);
            questionRepository.save(question);

            for (AnswerOptionRequest answerOptionRequest : questionRequest.getAnswerOptions()) {
                AnswerOption answerOption = new AnswerOption();
                answerOption.setText(answerOptionRequest.getText());
                answerOption.setStatus(answerOptionRequest.getStatus());
                answerOption.setQuestion(question);
                answerOptionRepository.save(answerOption);
            }
        }

        return survey;
    }

    @Transactional
    public List<Survey> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        System.out.println("Список анкет: " + surveys);  // Логируем анкеты
        return surveys;
    }

}
