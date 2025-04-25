package com.example.WebQuest.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class UserResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Survey survey;

    @ManyToOne
    private Question question;

    @ManyToMany
    private List<AnswerOption> selectedAnswers;

    private boolean correct;

    @ManyToOne
    private SurveySubmission submission; // Новое поле

    // Геттеры и сеттеры
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
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public SurveySubmission getSubmission() {
        return submission;
    }

    public void setSubmission(SurveySubmission submission) {
        this.submission = submission;
    }
}