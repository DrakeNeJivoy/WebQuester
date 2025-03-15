package com.example.WebQuest.controller;

import com.example.WebQuest.service.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailSender emailService;

    @GetMapping("/send")
    public String sendTestEmail(@RequestParam String to) {
        emailService.sendEmail(to, "Тестовое письмо", "Привет! Это тестовое сообщение из SimpleChat.");
        return "Письмо отправлено на " + to;
    }
}
