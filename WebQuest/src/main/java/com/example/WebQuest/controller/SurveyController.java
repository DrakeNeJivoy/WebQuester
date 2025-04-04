package com.example.WebQuest.controller;

import com.example.WebQuest.dto.SurveyRequest;
import com.example.WebQuest.model.Survey;
import com.example.WebQuest.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createSurvey(
            @RequestBody SurveyRequest surveyRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        Survey survey = surveyService.createSurvey(surveyRequest, userDetails.getUsername());
        return ResponseEntity.ok("Survey created with ID: " + survey.getId());
    }

    @PostMapping("/survey/delete/{id}")
    public ResponseEntity<String> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.ok("Survey with ID: " + id + " deleted successfully");
    }

    @GetMapping("/survey/{id}")
    public ResponseEntity<Survey> getSurvey(@PathVariable Long id) {
        Survey survey = surveyService.getSurveyWithQuestionsAndAnswers(id);
        if (survey == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(survey);
    }

    @PutMapping("/survey/{id}")
    public ResponseEntity<String> updateSurvey(@PathVariable Long id, @RequestBody SurveyRequest surveyRequest) {
        try {
            surveyService.updateSurvey(id, surveyRequest);
            return ResponseEntity.ok("Survey updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating survey: " + e.getMessage());
        }
    }
}