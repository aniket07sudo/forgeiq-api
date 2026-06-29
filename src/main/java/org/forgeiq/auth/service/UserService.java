package org.forgeiq.auth.service;

import lombok.RequiredArgsConstructor;
import org.forgeiq.auth.dto.UserResponse;
import org.forgeiq.auth.entity.User;
import org.forgeiq.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
