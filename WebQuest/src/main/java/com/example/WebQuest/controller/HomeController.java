package com.example.WebQuest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home"; // Это home.html в папке templates
    }

    @GetMapping("/home-create-survey")
    public String showCreateSurveyPage() {
        return "create-survey"; // Отображаем страницу создания анкеты
    }
}
