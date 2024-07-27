package com.book.backend.domain.auth.service;

import com.book.backend.domain.auth.dto.SignupDto;
import com.book.backend.domain.auth.mapper.AuthMapper;
import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.mapper.UserMapper;
import com.book.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;

    @Transactional
    public UserDto signup(SignupDto signupDto) {
        User user = authMapper.convertToUser(signupDto);
        user.setRegDate(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return userMapper.convertToUserDto(savedUser);
    }
}
