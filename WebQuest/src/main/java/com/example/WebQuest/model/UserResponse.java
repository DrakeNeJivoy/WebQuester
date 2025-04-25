package com.example.WebQuest.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_responses")
public class UserResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToMany
    @JoinTable(
            name = "user_selected_answers",
            joinColumns = @JoinColumn(name = "user_response_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_option_id")
    )
    private List<AnswerOption> selectedAnswers;

    private boolean isCorrect; // Был ли ответ на вопрос правильным

    public UserResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<AnswerOption> getSelectedAnswers() {
        return selectedAnswers;
    }

    public void setSelectedAnswers(List<AnswerOption> selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}