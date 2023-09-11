package org.diatliuk.bookstore.security;

import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.exception.EntityNotFoundException;
import org.diatliuk.bookstore.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a user by email: "
                                                                + username));
    }
}
