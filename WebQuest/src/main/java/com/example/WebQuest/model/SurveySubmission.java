package com.example.WebQuest.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class SurveySubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Survey survey;

    private LocalDateTime submissionDate;

    @OneToMany(mappedBy = "submission")
    private List<UserResponse> responses;

    // Конструкторы
    public SurveySubmission() {
        this.submissionDate = LocalDateTime.now();
    }

    public SurveySubmission(User user, Survey survey) {
        this.user = user;
        this.survey = survey;
        this.submissionDate = LocalDateTime.now();
    }

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

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public List<UserResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<UserResponse> responses) {
        this.responses = responses;
    }
}