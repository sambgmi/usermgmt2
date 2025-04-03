package com.example.userMgmt.service;

import com.example.userMgmt.dto.AuthResponse;
import com.example.userMgmt.dto.UpdateUserRequest;
import com.example.userMgmt.dto.UserResponse;
import com.example.userMgmt.model.User;
import com.example.userMgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return UserResponse.builder()
                .message("User details retrieved successfully")
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    @Transactional
    public UserResponse updateName(String currentEmail, String newName) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        user.setName(newName);
        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .message("Name updated successfully")
                .email(updatedUser.getEmail())
                .name(updatedUser.getName())
                .build();
    }

    @Transactional
    public UserResponse updateEmail(String currentEmail, String newEmail) {
        if (userRepository.existsByEmail(newEmail)) {
            throw new RuntimeException("Email already taken");
        }

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        user.setEmail(newEmail);
        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .message("Email updated successfully")
                .email(updatedUser.getEmail())
                .name(updatedUser.getName())
                .build();
    }

    @Transactional
    public UserResponse updateUser(String currentEmail, UpdateUserRequest request) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // Only check for email existence if email is being changed
        if (!request.getEmail().equals(currentEmail)) {
            boolean emailExists = userRepository.existsByEmail(request.getEmail());
            if (emailExists) {
                return UserResponse.builder()
                        .message("Email " + request.getEmail() + " is already taken")
                        .email(currentEmail)  // keep current email
                        .name(user.getName())  // keep current name
                        .build();
            }
        }

        // Update user details
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .message("User details updated successfully")
                .email(updatedUser.getEmail())
                .name(updatedUser.getName())
                .build();
    }

    @Transactional
    public UserResponse deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        String userName = user.getName();
        userRepository.delete(user);

        return UserResponse.builder()
                .message("User account deleted successfully")
                .email(email)
                .name(userName)
                .build();
    }
}