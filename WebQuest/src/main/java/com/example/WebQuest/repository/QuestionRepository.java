package com.example.WebQuest.repository;

import com.example.WebQuest.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionRepository extends JpaRepository<Question, Long> {
}
