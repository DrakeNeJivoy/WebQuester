package com.example.WebQuest.service;

import com.example.WebQuest.dto.*;
import com.example.WebQuest.model.*;
import com.example.WebQuest.repository.AnswerOptionRepository;
import com.example.WebQuest.repository.QuestionRepository;
import com.example.WebQuest.repository.SurveyRepository;
import com.example.WebQuest.repository.UserRepository;
import com.example.WebQuest.repository.UserResponseRepository;
import com.example.WebQuest.repository.SurveySubmissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final UserRepository userRepository;
    private final UserResponseRepository userResponseRepository;
    private final SurveySubmissionRepository surveySubmissionRepository; // Добавлено

    public SurveyService(SurveyRepository surveyRepository,
                         QuestionRepository questionRepository,
                         AnswerOptionRepository answerOptionRepository,
                         UserRepository userRepository,
                         UserResponseRepository userResponseRepository,
                         SurveySubmissionRepository surveySubmissionRepository) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.userRepository = userRepository;
        this.userResponseRepository = userResponseRepository;
        this.surveySubmissionRepository = surveySubmissionRepository;
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

    @Transactional
    public Long submitSurvey(Long surveyId, Map<Integer, List<Long>> answers, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        // Создаем новую запись о прохождении анкеты
        SurveySubmission submission = new SurveySubmission(user, survey);
        surveySubmissionRepository.save(submission);
        System.out.println("Создана SurveySubmission с ID: " + submission.getId());

        List<Question> questions = questionRepository.findBySurveyId(surveyId);

        for (int i = 1; i <= questions.size(); i++) {
            Question question = questions.get(i - 1);
            System.out.println("Обработка вопроса №" + i + " (ID: " + question.getId() + "): " + question.getText());
            List<Long> selectedAnswerIds = answers.get(i);
            List<AnswerOption> selectedAnswers = new ArrayList<>();
            boolean isCurrentQuestionCorrect = false;

            System.out.println("Выбранные ответы пользователя (ID): " + selectedAnswerIds);

            if (selectedAnswerIds != null && !selectedAnswerIds.isEmpty()) {
                selectedAnswers = answerOptionRepository.findAllById(selectedAnswerIds);
                List<AnswerOption> correctAnswers = answerOptionRepository.findByQuestionIdAndStatus(question.getId(), 2);
                System.out.println("Правильные ответы для вопроса (текст): " + correctAnswers.stream().map(AnswerOption::getText).collect(Collectors.toList()));
                if (!correctAnswers.isEmpty()) {
                    List<Long> correctIds = correctAnswers.stream().map(AnswerOption::getId).collect(Collectors.toList());
                    List<Long> selectedIds = selectedAnswers.stream().map(AnswerOption::getId).collect(Collectors.toList());
                    isCurrentQuestionCorrect = correctIds.equals(selectedIds);
                    System.out.println("Сравнение ID правильных ответов (" + correctIds + ") с выбранными (" + selectedIds + "): " + isCurrentQuestionCorrect);
                } else {
                    isCurrentQuestionCorrect = true; // Если нет правильных ответов, считаем правильным (спорно, но как есть)
                    System.out.println("Для вопроса нет правильных ответов, установлено isCurrentQuestionCorrect = true");
                }
            } else {
                if (!answerOptionRepository.findByQuestionIdAndStatus(question.getId(), 2).isEmpty()) {
                    isCurrentQuestionCorrect = false; // Если ничего не выбрано, а правильные ответы есть - неправильно
                    System.out.println("Пользователь не выбрал ответ, но есть правильные ответы, установлено isCurrentQuestionCorrect = false");
                } else {
                    isCurrentQuestionCorrect = true; // Если ничего не выбрано и нет правильных ответов - правильно (спорно)
                    System.out.println("Пользователь не выбрал ответ и нет правильных ответов, установлено isCurrentQuestionCorrect = true");
                }
            }

            UserResponse userResponse = new UserResponse();
            userResponse.setUser(user);
            userResponse.setSurvey(survey);
            userResponse.setQuestion(question);
            userResponse.setSelectedAnswers(selectedAnswers);
            userResponse.setCorrect(isCurrentQuestionCorrect);
            userResponse.setSubmission(submission); // Устанавливаем связь с SurveySubmission
            userResponseRepository.save(userResponse);
            System.out.println("Сохранен UserResponse для вопроса " + question.getId() + ", Correct = " + isCurrentQuestionCorrect);
        }

        return submission.getId(); // Возвращаем ID созданной SurveySubmission
    }
}