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

    // TODO: 리팩토링 필요
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByloginId(String loginId) throws UsernameNotFoundException {
        // username이 아닌 loginId로 간주
        log.trace("CustomUserDetailsService > loadUserByUsername()");

        User user = userRepository.findByLoginId(loginId)
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
                .password(user.getPassword())  // FIXME : 제거 가능한지 확인
                .authorities("ROLE_USER")
                .build();
    }

    public UserDetails loadUserByAppleId(String appleId) throws UsernameNotFoundException {
        log.trace("CustomUserDetailsService > loadUserByAppleId()");

        User user = userRepository.findByAppleId(appleId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getAppleId())
                .password(user.getPassword())  // FIXME : 제거 가능한지 확인
                .authorities("ROLE_USER")
                .build();
    }
}
