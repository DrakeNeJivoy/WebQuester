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
    // В SurveyService
    public List<Survey> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        System.out.println("Найденные анкеты: " + surveys);
        return surveys;
    }

    @Transactional
    public void deleteSurvey(Long id) {
        List<Question> questions = questionRepository.findBySurveyId(id);
        for (Question question : questions) {
            answerOptionRepository.deleteByQuestionId(question.getId()); // Удаляем варианты ответов
        }
        questionRepository.deleteBySurveyId(id); // Удаляем вопросы
        surveyRepository.deleteById(id); // Удаляем анкету
    }

    public Survey getSurveyWithQuestionsAndAnswers(Long id) {
        Survey survey = surveyRepository.findById(id).orElse(null);
        if (survey == null) {
            return null;
        }

        List<Question> questions = questionRepository.findBySurveyId(id);

        // Загружаем варианты ответов для каждого вопроса
        for (Question question : questions) {
            List<AnswerOption> answerOptions = answerOptionRepository.findByQuestionId(question.getId());
            // Добавляем варианты ответов в вопрос
            //question.setAnswerOptions(answerOptions);
        }
        //Возвращаем только анкету, вопросы и варианты ответов нужно получать отдельно
        return survey;
    }

    // SurveyService.java
    @Transactional
    public void updateSurvey(Long id, SurveyRequest surveyRequest) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("Survey not found"));
        survey.setTitle(surveyRequest.getTitle());
        surveyRepository.save(survey);

        // Удалить старые вопросы и ответы
        List<Question> oldQuestions = questionRepository.findBySurveyId(id);
        for(Question question : oldQuestions){
            answerOptionRepository.deleteByQuestionId(question.getId());
        }
        questionRepository.deleteBySurveyId(id);

        // Сохранить новые вопросы и ответы
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
    }
}
