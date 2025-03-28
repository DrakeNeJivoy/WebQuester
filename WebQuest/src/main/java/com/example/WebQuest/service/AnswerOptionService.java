package com.example.WebQuest.service;

import com.example.WebQuest.model.*;
import com.example.WebQuest.repository.AnswerOptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnswerOptionService {
    private final AnswerOptionRepository answerOptionRepository;

    public AnswerOptionService(AnswerOptionRepository answerOptionRepository) {
        this.answerOptionRepository = answerOptionRepository;
    }

    public List<AnswerOption> getAnswerOptionsByQuestionId(Long questionId) {
        return answerOptionRepository.findByQuestionId(questionId);
    }
}
