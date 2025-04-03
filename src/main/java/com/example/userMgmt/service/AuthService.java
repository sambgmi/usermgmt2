package com.example.userMgmt.service;

import com.example.userMgmt.dto.AuthResponse;
import com.example.userMgmt.dto.LoginRequest;
import com.example.userMgmt.dto.RegisterRequest;
import com.example.userMgmt.model.User;
import com.example.userMgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .message("User registered successfully")
                .email(user.getEmail())
                .name(user.getName())
                .build();  // Remove token generation here
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .message("Login successful")
                .email(user.getEmail())
                .name(user.getName())
                .token(token)  // Only generate token during login
                .build();
    }
}