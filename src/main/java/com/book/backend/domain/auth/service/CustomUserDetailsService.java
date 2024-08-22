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
        log.trace("CustomUserDetailsService > loadUserByUsername()");

        User user = userRepository.findByLoginId(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLoginId())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }

    public UserDetails loadUserByKakaoId(String kakaoId) throws UsernameNotFoundException {
        log.trace("CustomUserDetailsService > loadUserByKakaoId()");

        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getKakaoId())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
