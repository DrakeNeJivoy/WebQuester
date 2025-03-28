package com.example.WebQuest.controller;

import com.example.WebQuest.model.AnswerOption;
import com.example.WebQuest.model.Question;
import com.example.WebQuest.model.Survey;
import com.example.WebQuest.service.QuestionService;
import com.example.WebQuest.service.SurveyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
