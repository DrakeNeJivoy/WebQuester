package com.example.WebQuest.service;

import com.example.WebQuest.model.AnswerOption;
import com.example.WebQuest.model.Question;
import com.example.WebQuest.model.Survey;
import com.example.WebQuest.repository.AnswerOptionRepository;
import com.example.WebQuest.repository.QuestionRepository;
import com.example.WebQuest.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final SurveyRepository surveyRepository;

    public QuestionService(QuestionRepository questionRepository, AnswerOptionRepository answerOptionRepository, SurveyRepository surveyRepository) {
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.surveyRepository = surveyRepository;
    }

    public void addQuestion(Long surveyId, String questionText, List<String> answerOptions) {
        Optional<Survey> surveyOptional = surveyRepository.findById(surveyId);
        if (surveyOptional.isPresent()) {
            Survey survey = surveyOptional.get();
            Question question = new Question();
            question.setText(questionText);
            question.setSurvey(survey);
            questionRepository.save(question);

            for (String optionText : answerOptions) {
                AnswerOption option = new AnswerOption();
                option.setText(optionText);
                option.setQuestion(question);
                answerOptionRepository.save(option);
            }
        } else {
            throw new RuntimeException("Анкета не найдена!");
        }
    }

    public List<Question> getQuestionsBySurveyId(Long surveyId) {
        return questionRepository.findBySurveyId(surveyId);
    }

    public List<AnswerOption> getAnswerOptionsByQuestionId(Long questionId) {
        return answerOptionRepository.findByQuestionId(questionId);
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public boolean isCorrectlyAnswered(Question question, List<Long> selectedAnswerIds) {
        List<AnswerOption> correctAnswers = answerOptionRepository.findByQuestionIdAndStatus(question.getId(), 2);
        List<Long> correctIds = correctAnswers.stream().map(AnswerOption::getId).collect(Collectors.toList());
        return correctIds.equals(selectedAnswerIds);
    }
}