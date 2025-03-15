package com.example.WebQuest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "answer_options")
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text; // Текст ответа

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    private int status; // 0 - без правильного ответа, 1 - неправильный, 2 - правильный

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
