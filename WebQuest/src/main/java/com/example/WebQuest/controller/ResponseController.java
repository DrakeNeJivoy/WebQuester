package com.example.WebQuest.controller;

import com.example.WebQuest.model.AnswerOption;
import com.example.WebQuest.model.Question;
import com.example.WebQuest.model.Survey;
import com.example.WebQuest.model.SurveySubmission;
import com.example.WebQuest.service.QuestionService;
import com.example.WebQuest.service.SurveyService;
import com.example.WebQuest.service.SurveySubmissionService;
import com.example.WebQuest.service.UserResponseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin") // Или любое другое подходящее маппирование
public class ResponseController {

    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final SurveySubmissionService surveySubmissionService;
    private final UserResponseService userResponseService;

    public ResponseController(SurveyService surveyService, QuestionService questionService, SurveySubmissionService surveySubmissionService, UserResponseService userResponseService) {
        this.surveyService = surveyService;
        this.questionService = questionService;
        this.surveySubmissionService = surveySubmissionService;
        this.userResponseService = userResponseService;
    }

    @GetMapping("/survey/stats/{surveyId}")
    public String showSurveyStats(@PathVariable Long surveyId, Model model) {
        Survey survey = surveyService.getSurveyWithQuestionsAndAnswers(surveyId);
        if (survey == null) {
            return "error";
        }

        List<Question> questions = questionService.getQuestionsBySurveyId(surveyId);
        Map<Long, List<AnswerOption>> questionAnswerOptions = new HashMap<>();
        Map<Long, Map<Long, Long>> answerOptionCounts = new HashMap<>();
        Map<Long, Double> questionCorrectRatio = new HashMap<>();

        for (Question question : questions) {
            List<AnswerOption> options = questionService.getAnswerOptionsByQuestionId(question.getId());
            questionAnswerOptions.put(question.getId(), options);

            Map<Long, Long> counts = userResponseService.getAnswerOptionCountsForQuestion(question.getId(), surveyId);
            answerOptionCounts.put(question.getId(), counts);

            long totalResponses = surveySubmissionService.countSubmissionsForSurvey(surveyId);
            long correctResponses = userResponseService.countCorrectResponsesForQuestion(question.getId(), surveyId);
            double ratio = totalResponses > 0 ? (double) correctResponses / totalResponses : 0;
            questionCorrectRatio.put(question.getId(), ratio);
        }

        List<SurveySubmission> submissions = surveySubmissionService.getSubmissionsBySurveyIdWithUser(surveyId);

        model.addAttribute("survey", survey);
        model.addAttribute("questions", questions);
        model.addAttribute("questionAnswerOptions", questionAnswerOptions);
        model.addAttribute("answerOptionCounts", answerOptionCounts);
        model.addAttribute("questionCorrectRatio", questionCorrectRatio);
        model.addAttribute("submissions", submissions);

        return "survey-stats";
    }
}