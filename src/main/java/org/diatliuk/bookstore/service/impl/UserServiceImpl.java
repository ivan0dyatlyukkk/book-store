package org.diatliuk.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.user.UserLoginRequestDto;
import org.diatliuk.bookstore.dto.user.UserLoginResponseDto;
import org.diatliuk.bookstore.dto.user.UserRegistrationRequestDto;
import org.diatliuk.bookstore.dto.user.UserResponseDto;
import org.diatliuk.bookstore.repository.user.UserRepository;
import org.diatliuk.bookstore.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        return null;
    }

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        return null;
    }
}
