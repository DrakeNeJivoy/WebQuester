package com.example.WebQuest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя пользователя обязательно")
    @Column(unique = true, nullable = false)
    private String username;

    @Email
    @NotBlank(message = "Email обязателен")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private boolean verified = false;

    @Column(unique = true, nullable = true)
    private String token;

    @Column(nullable = false)
    private boolean isadmin = false; // Добавлено поле isAdmin, по умолчанию false (0)

    public boolean isConfirmed() {
        return verified;
    }

    public void setConfirmed(boolean value) {
        this.verified = value;
    }


    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public void setVerified(boolean confirmed) { this.verified = confirmed; }

    // В классе User
    public boolean isadmin() {
        System.out.println("isAdmin() вызван для пользователя с ID: " + this.id + " и isAdmin: " + this.isadmin);
        return isadmin;
    }
    public void setadmin(boolean admin) { this.isadmin = admin; }
}