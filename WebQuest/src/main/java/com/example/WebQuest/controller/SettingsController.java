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
public class SettingsController {

    @GetMapping("/settings")
    public String showSettings() {
        return "settings";
    }
}