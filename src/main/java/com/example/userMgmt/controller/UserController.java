package com.example.userMgmt.controller;

import com.example.userMgmt.dto.*;
import com.example.userMgmt.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getCurrentUser(authentication.getName()));
    }

    @PatchMapping("/me/name")
    public ResponseEntity<UserResponse> updateName(
            @Valid @RequestBody UpdateNameRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(userService.updateName(authentication.getName(), request.getName()));
    }

    @PatchMapping("/me/email")
    public ResponseEntity<UserResponse> updateEmail(
            @Valid @RequestBody UpdateEmailRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(userService.updateEmail(authentication.getName(), request.getEmail()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateBoth(
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(userService.updateUser(authentication.getName(), request));
    }

    @Operation(summary = "Delete current user account")
    @DeleteMapping("/me")
    public ResponseEntity<UserResponse> deleteAccount(Authentication authentication) {
        return ResponseEntity.ok(userService.deleteUser(authentication.getName()));
    }
}