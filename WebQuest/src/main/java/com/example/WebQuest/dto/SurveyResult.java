package com.example.WebQuest.dto;

import java.util.List;
import java.util.Map;

public class SurveyResult {
    private String surveyTitle;
    private List<QuestionResult> questionResults;
    private int totalQuestions;
    private int correctAnswers;

    // Конструкторы, геттеры и сеттеры

    public SurveyResult() {
    }

    public SurveyResult(String surveyTitle, List<QuestionResult> questionResults, int totalQuestions, int correctAnswers) {
        this.surveyTitle = surveyTitle;
        this.questionResults = questionResults;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public List<QuestionResult> getQuestionResults() {
        return questionResults;
    }

    public void setQuestionResults(List<QuestionResult> questionResults) {
        this.questionResults = questionResults;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}