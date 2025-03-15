package com.example.WebQuest.dto;

import lombok.Data;

@Data
public class AnswerOptionRequest {
    private String text;
    private int status = 0; // 0 - правильного ответа нет, 1 - неправильный, 2 - правильный

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
