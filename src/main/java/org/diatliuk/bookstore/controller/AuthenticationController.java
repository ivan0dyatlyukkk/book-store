package org.diatliuk.bookstore.controller;

import jakarta.validation.Valid;
import org.diatliuk.bookstore.dto.user.UserLoginRequestDto;
import org.diatliuk.bookstore.dto.user.UserLoginResponseDto;
import org.diatliuk.bookstore.dto.user.UserRegistrationRequestDto;
import org.diatliuk.bookstore.dto.user.UserResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestParam @Valid UserLoginRequestDto requestDto) {
        return null;
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestParam @Valid UserRegistrationRequestDto requestDto) {
        return null;
    }
}
