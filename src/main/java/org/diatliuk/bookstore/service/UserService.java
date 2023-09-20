package org.diatliuk.bookstore.service;

import org.diatliuk.bookstore.dto.user.UserRegistrationRequestDto;
import org.diatliuk.bookstore.dto.user.UserResponseDto;
import org.diatliuk.bookstore.exception.RegistrationException;
import org.diatliuk.bookstore.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    User getAuthenticatedUser();
}
