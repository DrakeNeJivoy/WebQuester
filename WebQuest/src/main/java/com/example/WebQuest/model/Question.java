package com.example.WebQuest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text; // Текст вопроса

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    public Survey getSurvey() { return survey; }
    public void setSurvey(Survey survey) { this.survey = survey; }

//    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<AnswerOption> answerOptions;
}
