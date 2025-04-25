package com.example.WebQuest.dto;

import com.example.WebQuest.model.AnswerOption;

import java.util.List;

public class QuestionResult {
    private String questionText;
    private List<AnswerOption> userAnswers;
    private List<AnswerOption> correctAnswers;
    private boolean isCorrect;

    // Конструкторы, геттеры и сеттеры

    public QuestionResult() {
    }

    public QuestionResult(String questionText, List<AnswerOption> userAnswers, List<AnswerOption> correctAnswers, boolean isCorrect) {
        this.questionText = questionText;
        this.userAnswers = userAnswers;
        this.correctAnswers = correctAnswers;
        this.isCorrect = isCorrect;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<AnswerOption> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<AnswerOption> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public List<AnswerOption> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<AnswerOption> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        this.isCorrect = correct;
    }
}