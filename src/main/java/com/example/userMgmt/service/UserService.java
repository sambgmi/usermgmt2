package com.example.userMgmt.service;

import com.example.userMgmt.dto.AuthResponse;
import com.example.userMgmt.model.User;
import com.example.userMgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public AuthResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return AuthResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .message("User details retrieved successfully")
                .build();
    }
}