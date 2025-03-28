package com.example.WebQuest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.WebQuest.service.SurveyService;
import org.springframework.ui.Model;
import com.example.WebQuest.model.Survey;
import java.util.List;

@Controller
public class HomeController {

    private final SurveyService surveyService;

    // Явное добавление конструктора
    public HomeController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Survey> surveys = surveyService.getAllSurveys();
        System.out.println("Количество анкет: " + surveys.size());  // Логируем количество анкет
        model.addAttribute("surveys", surveys);
        return "home"; // Это home.html в папке templates
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        List<Survey> surveys = surveyService.getAllSurveys();
        System.out.println("Количество анкет: " + surveys.size());  // Логируем количество анкет
        model.addAttribute("surveys", surveys);
        return "home";
    }

    @GetMapping("/home-create-survey")
    public String showCreateSurveyPage() {
        return "create-survey"; // Отображаем страницу создания анкеты
    }
}
