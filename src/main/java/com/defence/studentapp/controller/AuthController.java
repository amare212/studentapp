package com.defence.studentapp.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.defence.studentapp.dto.LoginRequest;
import com.defence.studentapp.dto.LoginResponse;
import com.defence.studentapp.dto.UserRequest;
import com.defence.studentapp.dto.UserResponse;
import com.defence.studentapp.entity.User;
import com.defence.studentapp.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public UserResponse register(
            @Valid @RequestBody UserRequest request) {

        User user = userService.registerUser(request);

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse loginResponse =
                userService.loginUser(request);

        ResponseCookie cookie = ResponseCookie
                .from("token", loginResponse.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(5))
                .sameSite("Lax")
                .build();

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        cookie.toString()
                )
                .body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        ResponseCookie cookie = ResponseCookie
                .from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        cookie.toString()
                )
                .body("Logout successful");
    }
}