package com.example.WebQuest.controller;

import com.example.WebQuest.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/add-question")
    public String addQuestion(@RequestParam("surveyId") Long surveyId,
                              @RequestParam("questionText") String questionText,
                              @RequestParam("answer1") String answer1,
                              @RequestParam("answer2") String answer2) {
        questionService.addQuestion(surveyId, questionText, Arrays.asList(answer1, answer2));
        return "redirect:/create-survey?surveyId=" + surveyId;
    }
}