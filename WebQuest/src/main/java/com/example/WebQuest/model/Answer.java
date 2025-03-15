package com.example.WebQuest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String text; // Текст ответа

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question; // К какому вопросу относится ответ

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
}
