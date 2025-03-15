package com.example.WebQuest.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {
    private String text;
    private List<AnswerOptionRequest> answerOptions;

    public String getText() { return text; }
    public void setTitle(String text) { this.text = text; }

    public List<AnswerOptionRequest> getAnswerOptions() { return answerOptions; }
    public void setAnswerOptions(List<AnswerOptionRequest> answerOptions) { this.answerOptions = answerOptions; }
}
