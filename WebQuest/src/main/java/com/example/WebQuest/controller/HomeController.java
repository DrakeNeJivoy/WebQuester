package com.example.WebQuest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.WebQuest.service.SurveyService;
import org.springframework.ui.Model;
import com.example.WebQuest.model.Survey;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.WebQuest.model.User;
import com.example.WebQuest.service.UserService;

@Controller
public class HomeController {

    private final SurveyService surveyService;
    private final UserService userService;

    public HomeController(SurveyService surveyService, UserService userService) {
        this.surveyService = surveyService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        return homePage(model);
    }

    // В HomeController
    // В HomeController
    @GetMapping("/home")
    public String homePage(Model model) {
        List<Survey> surveys = surveyService.getAllSurveys();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        User currentUser = userService.getUserByEmail(currentEmail);

        System.out.println("Текущий пользователь: " + currentUser);
        System.out.println("Список анкет: " + surveys);

        for (Survey survey : surveys) {
            System.out.println("Анкета ID: " + survey.getId() + ", Автор: " + survey.getUser());
        }

        if (currentUser != null) {
            System.out.println("Текущий пользователь ID: " + currentUser.getId());
            System.out.println("Текущий пользователь isadmin: " + currentUser.isadmin());
        }

        model.addAttribute("surveys", surveys);
        model.addAttribute("currentUser", currentUser);
        return "home";
    }
    @GetMapping("/home-create-survey")
    public String showCreateSurveyPage() {
        return "create-survey"; // Отображаем страницу создания анкеты
    }
}