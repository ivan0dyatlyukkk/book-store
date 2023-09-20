package org.diatliuk.bookstore.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.user.UserRegistrationRequestDto;
import org.diatliuk.bookstore.dto.user.UserResponseDto;
import org.diatliuk.bookstore.enums.RoleName;
import org.diatliuk.bookstore.exception.EntityNotFoundException;
import org.diatliuk.bookstore.exception.RegistrationException;
import org.diatliuk.bookstore.mapper.UserMapper;
import org.diatliuk.bookstore.model.Role;
import org.diatliuk.bookstore.model.ShoppingCart;
import org.diatliuk.bookstore.model.User;
import org.diatliuk.bookstore.repository.cart.ShoppingCartRepository;
import org.diatliuk.bookstore.repository.role.RoleRepository;
import org.diatliuk.bookstore.repository.user.UserRepository;
import org.diatliuk.bookstore.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final RoleName DEFAULT_ROLE = RoleName.ROLE_USER;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
                                            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration!");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role defaultRole = roleRepository
                .findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new EntityNotFoundException("The role: "
                                                            + DEFAULT_ROLE + " does not exist"));
        user.setRoles(Set.of(defaultRole));
        User savedUser = userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(savedUser);
        shoppingCartRepository.save(shoppingCart);

        return userMapper.toDto(savedUser);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Can't find a user by email: "
                        + auth.getName()));
    }
}
