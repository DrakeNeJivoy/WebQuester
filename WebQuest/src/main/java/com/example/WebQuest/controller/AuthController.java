package com.example.WebQuest.controller;

import com.example.WebQuest.model.User;
import com.example.WebQuest.repository.UserRepository;
import com.example.WebQuest.service.UserService;
import com.example.WebQuest.service.EmailSender;
import com.example.WebQuest.dto.LoginRequest;
//import com.example.auth.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;
import java.util.UUID;

@Controller
//@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailService;
    private final UserRepository authRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    public AuthController(UserService userService, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, EmailSender emailService,
   //                       JavaMailSender mailSender,
    UserRepository authRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authRepository = authRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }



    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        if (userService.registerUser(username, email, password)) {
            model.addAttribute("message", "Check your email to confirm your account!");
            return "register"; // Показываем страницу с сообщением
        } else {
            model.addAttribute("error", "User already exists!");
            return "register";
        }
    }

    @GetMapping("/confirm-email")
    public String confirmEmail(@RequestParam String token, Model model) {
        System.out.println("Маршрут /confirm-email вызван с токеном: " + token); // Логирование

        boolean isConfirmed = userService.confirmEmail(token);
        if (isConfirmed) {
            model.addAttribute("message", "Ваш email успешно подтверждён! Теперь вы можете войти.");
        } else {
            model.addAttribute("error", "Неверный или просроченный токен!");
        }

        return "confirm-email"; // Отображаем confirm-email.html
    }



    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        System.out.println("Открыта страница логина");

        if (error != null) {
            System.out.println("Ошибка входа!");
            model.addAttribute("errorMessage", "Неверное имя пользователя или пароль!");
        }
        if (logout != null) {
            System.out.println("Выход выполнен");
            model.addAttribute("successMessage", "Вы успешно вышли из системы.");
        }

        return "login";
    }
}
