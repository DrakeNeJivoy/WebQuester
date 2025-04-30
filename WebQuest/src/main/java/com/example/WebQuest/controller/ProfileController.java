package com.example.WebQuest.controller;

import com.example.WebQuest.model.SurveySubmission;
import com.example.WebQuest.model.User;
import com.example.WebQuest.service.SurveySubmissionService;
import com.example.WebQuest.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final SurveySubmissionService surveySubmissionService;

    public ProfileController(UserService userService, SurveySubmissionService surveySubmissionService) {
        this.userService = userService;
        this.surveySubmissionService = surveySubmissionService;
    }

    @GetMapping
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Получаем email пользователя

        model.addAttribute("email", email); // Добавляем email в модель

        User user = userService.getUserByEmail(email); // Получаем пользователя по email
        if (user != null) {
            List<SurveySubmission> submissions = surveySubmissionService.getSubmissionsByUserIdWithSurvey(user.getId());
            model.addAttribute("submissions", submissions);
        }

        return "profile";
    }
}