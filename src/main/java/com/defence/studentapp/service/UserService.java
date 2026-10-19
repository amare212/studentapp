
package com.defence.studentapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.defence.studentapp.dto.LoginRequest;
import com.defence.studentapp.dto.LoginResponse;
import com.defence.studentapp.dto.UserRequest;
import com.defence.studentapp.entity.User;
import com.defence.studentapp.repository.UserRepository;
import com.defence.studentapp.security.JwtService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================
    // REGISTER USER
    // =========================
    public User registerUser(UserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // Hash the password before saving
        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        user.setPassword(encodedPassword);

        // Default role
        user.setRole("USER");

        return userRepository.save(user);
    }

    // =========================
    // LOGIN USER
    // =========================
    public LoginResponse loginUser(LoginRequest request) {

        // 1. Find user by username
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid username or password"));

        // 2. Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid username or password");
        }

        // 3. Generate JWT token
        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole()
        );

        // 4. Return login response with JWT token
        return new LoginResponse(
                "Login successful",
                user.getId(),
                user.getUsername(),
                user.getRole(),
                token
        );
    }
}

