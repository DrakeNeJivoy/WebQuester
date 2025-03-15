package com.example.WebQuest.repository;

import com.example.WebQuest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    //Optional<User> findByVerificationToken(String token);
    //User findByUsername(String username);
    //User findByEmail(String email);
    //User findByUsernameAndPassword(String username, String password);
    //User findByToken(String token);
    Optional<User> findByUsername(String username);
    //Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token); // <-- Добавь этот метод!
}