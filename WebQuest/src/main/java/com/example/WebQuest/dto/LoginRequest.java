package com.example.WebQuest.dto;


public class LoginRequest {
    private String username;
    private String password;
    private String email;
    private String token;
    public void setToken(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
