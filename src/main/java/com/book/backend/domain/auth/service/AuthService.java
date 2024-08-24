package com.book.backend.domain.auth.service;

import com.book.backend.domain.auth.dto.JwtTokenDto;
import com.book.backend.domain.auth.dto.LoginDto;
import com.book.backend.domain.auth.dto.LoginSuccessResponseDto;
import com.book.backend.domain.auth.dto.SignupDto;
import com.book.backend.domain.auth.mapper.AuthMapper;
import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.mapper.UserMapper;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import com.book.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtRefreshTokenService jwtRefreshTokenService;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserDto signup(SignupDto signupDto) {
        log.trace("AuthService > signup()");

        validateNotDuplicatedUsername(signupDto.getLoginId());

        User user = authMapper.convertToUser(signupDto);
        user.setRegDate(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return userMapper.convertToUserDto(savedUser);
    }

    @Transactional
    public LoginSuccessResponseDto login(LoginDto loginDto) {
        log.trace("AuthService > login()");

        try {
            // 사용자 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword()));

            // 인증 성공 시 Security Context에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 인증 성공 후 유저 정보 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getLoginId());
        JwtTokenDto jwtTokenDto = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByLoginId(loginDto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // RefreshToken 갱신
        jwtRefreshTokenService.updateRefreshToken(jwtTokenDto, user);

        return LoginSuccessResponseDto.builder()
                .userId(user.getUserId())
                .accessToken(jwtTokenDto.getAccessToken())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .build();
    }

    @Transactional
    public void deleteAccountByUsername(String username) {
        log.trace("AuthService > deleteAccountByLoginId()");

        User user = userService.findByUsername(username);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.delete(user);
    }

    private void validateNotDuplicatedUsername(String loginId) {
        log.trace("AuthService > validateNotDuplicatedByUsername()");

        User user = userService.findByUsername(loginId);
        if (user != null) {
            throw new CustomException(ErrorCode.USERNAME_DUPLICATED);
        }
    }
}


