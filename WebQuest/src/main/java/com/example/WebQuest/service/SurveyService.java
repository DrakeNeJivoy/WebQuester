package com.example.WebQuest.service;

import com.example.WebQuest.dto.*;
import com.example.WebQuest.model.*;
import com.example.WebQuest.repository.AnswerOptionRepository;
import com.example.WebQuest.repository.QuestionRepository;
import com.example.WebQuest.repository.SurveyRepository;
import com.example.WebQuest.repository.UserRepository;
import com.example.WebQuest.repository.UserResponseRepository;
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

    public SurveyService(SurveyRepository surveyRepository,
                         QuestionRepository questionRepository,
                         AnswerOptionRepository answerOptionRepository,
                         UserRepository userRepository,
                         UserResponseRepository userResponseRepository) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.userRepository = userRepository;
        this.userResponseRepository = userResponseRepository;
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
    public String submitSurvey(Long surveyId, Map<Integer, List<Long>> answers, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
        List<Question> questions = questionRepository.findBySurveyId(surveyId);

        for (int i = 1; i <= questions.size(); i++) {
            Question question = questions.get(i - 1);
            List<Long> selectedAnswerIds = answers.get(i); // Получаем ID выбранных ответов для текущего вопроса
            List<AnswerOption> selectedAnswers = new ArrayList<>();
            boolean isCurrentQuestionCorrect = false;

            if (selectedAnswerIds != null && !selectedAnswerIds.isEmpty()) {
                selectedAnswers = answerOptionRepository.findAllById(selectedAnswerIds);

                // Проверяем, есть ли правильные ответы для этого вопроса
                List<AnswerOption> correctAnswers = answerOptionRepository.findByQuestionIdAndStatus(question.getId(), 2);

                if (!correctAnswers.isEmpty()) {
                    // Если есть правильные ответы, сравниваем выбранные с ними
                    List<Long> correctIds = correctAnswers.stream().map(AnswerOption::getId).collect(Collectors.toList());
                    List<Long> selectedIds = selectedAnswers.stream().map(AnswerOption::getId).collect(Collectors.toList());
                    isCurrentQuestionCorrect = correctIds.equals(selectedIds);
                } else {
                    // Если правильных ответов нет, считаем ответ всегда "правильным" (или можно настроить другую логику)
                    isCurrentQuestionCorrect = true;
                }
            } else {
                // Если пользователь не выбрал ни одного варианта, считаем ответ неправильным (если есть правильные ответы)
                if (!answerOptionRepository.findByQuestionIdAndStatus(question.getId(), 2).isEmpty()) {
                    isCurrentQuestionCorrect = false;
                } else {
                    isCurrentQuestionCorrect = true; // Если нет правильных ответов, отсутствие выбора можно считать "правильным"
                }
            }

            UserResponse userResponse = new UserResponse();
            userResponse.setUser(user);
            userResponse.setSurvey(survey);
            userResponse.setQuestion(question);
            userResponse.setSelectedAnswers(selectedAnswers);
            userResponse.setCorrect(isCurrentQuestionCorrect);
            userResponseRepository.save(userResponse);
        }

        return "Анкета успешно отправлена!";
    }
}