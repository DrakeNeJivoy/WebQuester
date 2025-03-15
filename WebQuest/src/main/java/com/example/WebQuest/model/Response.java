package com.example.WebQuest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "responses")
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Кто ответил

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer; // Какой вариант ответа выбрал пользователь

    public Answer getAnswer() { return answer; }
    public void setAnswer(Answer answer) { this.answer = answer; }
}

