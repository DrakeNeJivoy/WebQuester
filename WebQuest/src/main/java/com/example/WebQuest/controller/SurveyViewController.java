package com.example.WebQuest.controller;

import com.example.WebQuest.model.AnswerOption;
import com.example.WebQuest.model.Question;
import com.example.WebQuest.model.Survey;
import com.example.WebQuest.model.SurveySubmission;
import com.example.WebQuest.model.UserResponse;
import com.example.WebQuest.service.QuestionService;
import com.example.WebQuest.service.SurveyService;
import com.example.WebQuest.service.SurveySubmissionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SurveyViewController {

    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final SurveySubmissionService surveySubmissionService;

    public SurveyViewController(SurveyService surveyService, QuestionService questionService, SurveySubmissionService surveySubmissionService) {
        this.surveyService = surveyService;
        this.questionService = questionService;
        this.surveySubmissionService = surveySubmissionService;
    }

    @GetMapping("/survey/{id}")
    public String viewSurvey(@PathVariable Long id, Model model) {
        System.out.println("Получен запрос на отображение анкеты с ID: " + id);

        Survey survey = surveyService.getSurveyById(id);
        if (survey == null) {
            System.out.println("Анкета с ID " + id + " не найдена!");
            return "error";
        }
        System.out.println("Найдена анкета: " + survey.getTitle());

        // Загружаем вопросы анкеты
        List<Question> questions = questionService.getQuestionsBySurveyId(id);
        System.out.println("Загружено " + questions.size() + " вопросов для анкеты с ID " + id);

        // Загружаем варианты ответов для каждого вопроса
        Map<Long, List<AnswerOption>> questionAnswerOptions = new HashMap<>();
        for (Question question : questions) {
            List<AnswerOption> options = questionService.getAnswerOptionsByQuestionId(question.getId());
            System.out.println("Загружено " + options.size() + " вариантов ответов для вопроса ID " + question.getId());

            for (AnswerOption option : options) {
                System.out.println("Ответ: " + option.getText());
            }

            questionAnswerOptions.put(question.getId(), options);
        }

        model.addAttribute("survey", survey);
        model.addAttribute("questions", questions);
        model.addAttribute("questionAnswerOptions", questionAnswerOptions);


        System.out.println("Передача модели в шаблон:");
        System.out.println("questionAnswerOptions: " + questionAnswerOptions);
        return "survey-view";
    }

    @PostMapping("/survey/{id}/submit")
    @ResponseBody
    public Map<String, Long> submitSurvey(@PathVariable Long id, HttpServletRequest request) {
        System.out.println("POST /survey/" + id + "/submit");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        System.out.println("Пользователь: " + userEmail);
        Map<Integer, List<Long>> userAnswers = new HashMap<>();
        for (String paramName : request.getParameterMap().keySet()) {
            if (paramName.startsWith("answers[")) {
                try {
                    Integer questionNumber = Integer.parseInt(paramName.replaceAll("[^\\d]", ""));
                    String[] values = request.getParameterValues(paramName);
                    List<Long> ids = new ArrayList<>();
                    if (values != null) {
                        for (String value : values) {
                            try {
                                ids.add(Long.parseLong(value));
                            } catch (NumberFormatException e) {
                                System.err.println("Некорректный ID ответа: " + value);
                            }
                        }
                    }
                    userAnswers.put(questionNumber, ids);
                    System.out.println("Контроллер - Ответы пользователя на вопрос " + questionNumber + ": " + ids);
                } catch (NumberFormatException e) {
                    System.err.println("Контроллер - Некорректный номер вопроса в параметре: " + paramName);
                }
            }
        }
        System.out.println("Контроллер - Все ответы пользователя: " + userAnswers);
        Long submissionId = surveyService.submitSurvey(id, userAnswers, userEmail);
        System.out.println("Контроллер - Создана отправка анкеты с ID: " + submissionId);
        Map<String, Long> response = new HashMap<>();
        response.put("submissionId", submissionId);
        return response;
    }

    @GetMapping("/submission-result/{submissionId}")
    public String showSubmissionResult(@PathVariable Long submissionId, Model model) {
        System.out.println("GET /submission-result/" + submissionId);
        System.out.println("Запрошен ID отправки: " + submissionId);

        SurveySubmission submission = surveySubmissionService.getSubmissionWithResponses(submissionId);
        if (submission == null) {
            System.out.println("SurveySubmission с ID " + submissionId + " не найден!");
            return "error";
        }
        System.out.println("Найдена SurveySubmission: " + submission);

        Survey survey = surveyService.getSurveyById(submission.getSurvey().getId());
        List<Question> questions = questionService.getQuestionsBySurveyId(survey.getId());
        Map<Long, List<AnswerOption>> allAnswerOptions = new HashMap<>();
        Map<Long, List<Long>> userSelectedAnswerIds = new HashMap<>();
        Map<Long, Boolean> questionCorrectness = new HashMap<>(); // Вопрос ID -> Правильно ли ответил пользователь

        for (Question question : questions) {
            List<AnswerOption> options = questionService.getAnswerOptionsByQuestionId(question.getId());
            allAnswerOptions.put(question.getId(), options);

            List<UserResponse> responsesForQuestion = submission.getResponses().stream()
                    .filter(response -> response.getQuestion().getId().equals(question.getId()))
                    .collect(Collectors.toList());

            List<Long> selectedIdsForQuestion = responsesForQuestion.stream()
                    .flatMap(response -> response.getSelectedAnswers().stream().map(AnswerOption::getId))
                    .collect(Collectors.toList());
            userSelectedAnswerIds.put(question.getId(), selectedIdsForQuestion);
            System.out.println("Вопрос ID: " + question.getId() + ", Выбранные ответы ID: " + selectedIdsForQuestion);

            // Определяем, был ли ответ на вопрос правильным
            boolean correctForQuestion = responsesForQuestion.stream()
                    .anyMatch(UserResponse::isCorrect);
            questionCorrectness.put(question.getId(), correctForQuestion);
            System.out.println("Вопрос ID: " + question.getId() + ", Правильно ответил: " + correctForQuestion);
        }

        model.addAttribute("submission", submission);
        model.addAttribute("survey", survey);
        model.addAttribute("questions", questions);
        model.addAttribute("allAnswerOptions", allAnswerOptions);
        model.addAttribute("userSelectedAnswerIds", userSelectedAnswerIds);
        model.addAttribute("questionCorrectness", questionCorrectness); // Передаем правильность ответа

        System.out.println("Передаю модель для /submission-result/" + submissionId);
        return "submission-result";
    }
}