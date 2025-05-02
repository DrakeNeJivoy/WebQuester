package com.example.WebQuest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
public class AboutController {

    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    @GetMapping("/about")
    public String showSettings(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login"; // Перенаправление на логин, если пользователь не аутентифицирован
        }
        System.out.println("Загрузка о нас");
        return "about";
    }
}