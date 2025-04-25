//package com.example.WebQuest.service;
//
//import com.example.WebQuest.model.UserResponse;
//import com.example.WebQuest.repository.UserResponseRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class UserResponseService {
//
//    private final UserResponseRepository userResponseRepository;
//
//    public UserResponseService(UserResponseRepository userResponseRepository) {
//        this.userResponseRepository = userResponseRepository;
//    }
//
//    @Transactional(readOnly = true)
//    public UserResponse getUserResponseWithDetails(Long id) {
//        return userResponseRepository.findByIdWithDetails(id).orElse(null);
//    }
//}