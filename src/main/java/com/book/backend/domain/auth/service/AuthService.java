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
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public UserDto signup(SignupDto signupDto) {
        log.trace("AuthService > signup()");

        userService.validateNotDuplicatedUsername(signupDto.getLoginId());
        userService.validateNotDuplicatedNickname(signupDto.getNickname());

        User user = authMapper.convertToUser(signupDto);
        user.setRegDate(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return userMapper.convertToUserDto(savedUser);
    }

    @Transactional
    public LoginSuccessResponseDto login(LoginDto loginDto) {
        log.trace("AuthService > login()");

        Authentication authentication;
        try {
            // 사용자 인증 시도
            authentication = authenticationManager.authenticate(
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

        // Redis에 RefreshToken 저장
        jwtUtil.storeRefreshTokenInRedis(authentication, jwtTokenDto.getRefreshToken());

        // FIXME: 수정 필요
        // RefreshToken 갱신
        jwtRefreshTokenService.updateRefreshToken(jwtTokenDto, user);

        return LoginSuccessResponseDto.builder()
                .userId(user.getUserId())
                .accessToken(jwtTokenDto.getAccessToken())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .build();
    }

    public JwtTokenDto reissueToken(String refreshToken) {
        // Refresh Token 검증
        Claims claims = jwtUtil.getAllClaims(refreshToken);
        String username = String.valueOf(claims.get("username"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String redisRefreshToken = redisTemplate.opsForValue().get(username);

        if (!redisRefreshToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.NOT_EXIST_REFRESH_TOKEN);
        }

        // 토큰 재발행
        JwtTokenDto tokenDto = jwtUtil.generateToken(userDetails);
        jwtUtil.storeRefreshTokenInRedis(authentication, refreshToken);

        return tokenDto;
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

}


