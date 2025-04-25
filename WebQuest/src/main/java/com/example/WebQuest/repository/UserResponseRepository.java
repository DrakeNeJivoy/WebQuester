package com.example.WebQuest.repository;

import com.example.WebQuest.model.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserResponseRepository extends JpaRepository<UserResponse, Long> {
}