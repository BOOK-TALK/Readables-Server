package com.book.backend.domain.auth.service;

import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username이 아닌 loginId로 간주
        log.trace("CustomUserDetailsService > loadUserByUsername()");

        User user = userRepository.findByLoginId(username)
                .orElseGet(() -> userRepository.findByKakaoId(username)
                        .orElseGet(() -> userRepository.findByAppleId(username)
                                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))));

        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
