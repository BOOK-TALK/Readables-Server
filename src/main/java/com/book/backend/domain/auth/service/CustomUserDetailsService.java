package com.book.backend.domain.auth.service;

import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLoginId())
                .password(user.getPassword())
                .authorities("ROLE_USER")  // 사용자 권한 설정
                .build();
    }
}
