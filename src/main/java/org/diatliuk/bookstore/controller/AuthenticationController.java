package org.diatliuk.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.user.UserLoginRequestDto;
import org.diatliuk.bookstore.dto.user.UserLoginResponseDto;
import org.diatliuk.bookstore.dto.user.UserRegistrationRequestDto;
import org.diatliuk.bookstore.dto.user.UserResponseDto;
import org.diatliuk.bookstore.exception.RegistrationException;
import org.diatliuk.bookstore.security.AuthenticationService;
import org.diatliuk.bookstore.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User's authentication", description = "Endpoints for logging in and registering users")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Login an existed user")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
                                                                throws RegistrationException {
        return userService.register(requestDto);
    }
}
