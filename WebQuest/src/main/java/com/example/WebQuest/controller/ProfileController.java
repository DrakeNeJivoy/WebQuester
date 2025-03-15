package com.example.WebQuest.controller;

import com.example.WebQuest.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login"; // Перенаправление на логин, если пользователь не аутентифицирован
        }

        model.addAttribute("email", userDetails.getUsername()); // Добавляем email пользователя в модель
        return "profile"; // Это profile.html в папке templates
    }
}
