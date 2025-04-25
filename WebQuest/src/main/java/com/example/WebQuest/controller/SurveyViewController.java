package com.example.WebQuest.controller;

import com.example.WebQuest.model.AnswerOption;
import com.example.WebQuest.model.Question;
import com.example.WebQuest.model.Survey;
import com.example.WebQuest.service.QuestionService;
import com.example.WebQuest.service.SurveyService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SurveyViewController {

    private final SurveyService surveyService;
    private final QuestionService questionService;

    public SurveyViewController(SurveyService surveyService, QuestionService questionService) {
        this.surveyService = surveyService;
        this.questionService = questionService;
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
    public String submitSurvey(@PathVariable Long id, HttpServletRequest request, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        System.out.println("Получен POST запрос на /survey/" + id + "/submit");
        System.out.println("Email пользователя: " + userEmail);

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
                    System.out.println("Ответы пользователя на вопрос " + questionNumber + ": " + ids);
                } catch (NumberFormatException e) {
                    System.err.println("Некорректный номер вопроса в параметре: " + paramName);
                }
            }
        }

        System.out.println("Все ответы пользователя: " + userAnswers);

        String submissionResult = surveyService.submitSurvey(id, userAnswers, userEmail);
        // На этом этапе мы просто получаем сообщение об успешной отправке
        // и не передаем никаких данных для отображения результатов.
        return "submission-result";
    }

    @GetMapping("/submission-result")
    public String showSubmissionResult() {
        System.out.println("Получен GET запрос на /submission-result");
        return "submission-result";
    }
}