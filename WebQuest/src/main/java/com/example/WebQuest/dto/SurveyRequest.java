package com.example.WebQuest.dto;

import lombok.Data;

import java.util.List;

@Data
public class SurveyRequest {
    private String title;
    private List<QuestionRequest> questions;

    public String getTitle() { return title; }
    public void setTitle(String username) { this.title = username; }

    public List<QuestionRequest> getQuestions() { return questions; }
    public void setQuestions(List<QuestionRequest> questions) { this.questions = questions; }
}
