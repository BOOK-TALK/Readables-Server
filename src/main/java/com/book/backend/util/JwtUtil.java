package com.book.backend.util;

import com.book.backend.domain.auth.dto.JwtTokenDto;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secretKey}")
    private String secret;
    @Value("${jwt.accessTokenExpireTime}")
    private Long accessTokenExpireTime;
    @Value("${jwt.refreshTokenExpireTime}")
    private Long refreshTokenExpireTime;
    private final RedisTemplate<String, String> redisTemplate;

    // 토큰 생성
    public JwtTokenDto generateToken(UserDetails userDetails) {
        Claims claims = Jwts.claims();
        claims.put("username", userDetails.getUsername());

        return JwtTokenDto.builder()
                .accessToken(createAccessToken(claims))
                .refreshToken(createRefreshToken(claims))
                .build();
    }

    // 개발용, 커스텀 유효기간 토큰 생성
    public JwtTokenDto generateCustomToken(UserDetails userDetails) {
        Claims claims = Jwts.claims();
        claims.put("username", userDetails.getUsername());

        return JwtTokenDto.builder()
                .accessToken(createCustomAccessToken(claims))
                .refreshToken(createRefreshToken(claims))
                .build();
    }

    private String createAccessToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpireTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // 개발용, 커스텀 엑세스 토큰 생성
    private String createCustomAccessToken(Claims claims) {
        // 개발용, 커스텀 유효기간
        long customAccessTokenExpireTime = 180000L;
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + customAccessTokenExpireTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private String createRefreshToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpireTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // 토큰 유효 여부 확인 (by userDetails)
    public Boolean isValidToken(String token, UserDetails userDetails) {
        log.info("isValidToken token = {}", token);
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 토큰에서 모든 클레임 추출
    public Claims getAllClaims(String token) {
        log.info("getAllClaims token = {}", token);
        return Jwts.parser()
                .setSigningKey(secret)  // secret 키로 검증
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getAllClaimsByPublicKey(String token, PublicKey publicKey) {
        try {
            return Jwts.parser()
                    .setSigningKey(publicKey)  // public 키로 검증
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.JWT_EXPIRED);
        }
    }

    // 토큰에서 username 추출하여 반환
    public String getUsernameFromToken(String token) {
        String username = String.valueOf(getAllClaims(token).get("username"));
        log.info("getUsernameFormToken subject = {}", username);
        return username;
    }

    // 토큰 만료 기한 추출하여 반환
    public Date getExpirationDate(String token) {
        Claims claims = getAllClaims(token);
        return claims.getExpiration();
    }

    // 토큰이 만료되었는지
    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    // Redis에 RefreshToken 저장
    public void storeRefreshTokenInRedis(Authentication authentication, String refreshToken) {
        log.trace("JwtUtil > storeRefreshTokenInRedis()");
        redisTemplate.opsForValue().set(
                authentication.getName(),
                refreshToken,
                refreshTokenExpireTime,
                TimeUnit.MILLISECONDS
        );
    }

    // 블랙리스트에 토큰 저장
    public void addTokenToBlacklist(String token) {
        // 토큰의 만료 시간 계산
        long expirationTime = getExpirationDate(token).getTime();
        long currentTime = System.currentTimeMillis();
        long ttl = expirationTime - currentTime;

        log.trace(String.valueOf(ttl));

        if (ttl > 0) {
            // 블랙리스트에 저장
            redisTemplate.opsForValue().set(
                    token,
                    "blacklisted",
                    ttl,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    public String getAccessTokenByHttpRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        String token = "";
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        } else {
            throw new CustomException(ErrorCode.JWT_NOT_FOUND);
        }

        return token;
    }
}
