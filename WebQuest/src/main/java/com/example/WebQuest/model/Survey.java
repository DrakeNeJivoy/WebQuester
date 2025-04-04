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
@Table(name = "surveys")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Long getId() { return id; }

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

}
