package com.book.backend.domain.auth.service;

import com.book.backend.domain.auth.dto.JwtTokenDto;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import com.book.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public void logout(HttpServletRequest request) {
        log.trace("AuthService > logout()");

        String accessToken = jwtUtil.getAccessTokenByHttpRequest(request);
        String username = jwtUtil.getUsernameFromToken(accessToken);

        jwtUtil.addTokenToBlacklist(accessToken);

        // Redis에서 사용자의 Refresh Token 삭제
        redisTemplate.delete(username);

        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void deleteUser(HttpServletRequest request) {
        log.trace("AuthService > deleteUser()");

        String accessToken = jwtUtil.getAccessTokenByHttpRequest(request);
        String username = jwtUtil.getUsernameFromToken(accessToken);

        jwtUtil.addTokenToBlacklist(accessToken);

        // Redis에서 사용자의 Refresh Token 삭제
        redisTemplate.delete(username);

        // 회원 엔티티 삭제
        User user = userService.findByUsername(username);
        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        SecurityContextHolder.clearContext();
    }

    public JwtTokenDto reissueToken(String refreshToken) {
        // Refresh Token 검증
        Claims claims = jwtUtil.getAllClaims(refreshToken);
        String username = String.valueOf(claims.get("username"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String redisRefreshToken = redisTemplate.opsForValue().get(username);

        log.trace("username: " + username);
        log.trace("refreshToken:      " + refreshToken);
        log.trace("redisRefreshToken: " + redisRefreshToken);

        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.NOT_EXIST_REFRESH_TOKEN);
        }

        // 토큰 재발행
        JwtTokenDto tokenDto = jwtUtil.generateToken(userDetails);
        jwtUtil.storeRefreshTokenInRedis(authentication, tokenDto.getRefreshToken());

        return tokenDto;
    }

}
