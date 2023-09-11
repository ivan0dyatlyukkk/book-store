package org.diatliuk.bookstore.service;

import org.diatliuk.bookstore.dto.user.UserLoginRequestDto;
import org.diatliuk.bookstore.dto.user.UserLoginResponseDto;
import org.diatliuk.bookstore.dto.user.UserRegistrationRequestDto;
import org.diatliuk.bookstore.dto.user.UserResponseDto;
import org.diatliuk.bookstore.exception.RegistrationException;

public interface UserService {
    UserLoginResponseDto login(UserLoginRequestDto requestDto);

    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
