package org.forgeiq.auth.controller;

import lombok.RequiredArgsConstructor;
import org.forgeiq.auth.dto.AuthResponse;
import org.forgeiq.auth.dto.LoginRequest;
import org.forgeiq.auth.dto.SingupRequestDto;
import org.forgeiq.auth.dto.UserResponse;
import org.forgeiq.auth.security.UserPrincipal;
import org.forgeiq.auth.service.AuthService;
import org.forgeiq.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> singnup(@RequestBody SingupRequestDto request) {
        AuthResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String email = principal.getEmail();
        return userService.getCurrentUser(email);
    }
}
