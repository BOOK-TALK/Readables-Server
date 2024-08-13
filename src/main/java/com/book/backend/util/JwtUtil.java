package com.book.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secretKey}")
    private String secret;

    // 토큰 생성
    public String generateToken(UserDetails userDetails) {
        Claims claims = Jwts.claims();
        claims.put("username", userDetails.getUsername());
        return createToken(claims); // username을 subject로 해서 token 생성
    }

    private String createToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 1시간
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // 토큰 유효 여부 확인
    public Boolean isValidToken(String token, UserDetails userDetails) {
        log.info("isValidToken token = {}", token);
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 토큰에서 모든 클레임 추출
    private Claims getAllClaims(String token) {
        log.info("getAllClaims token = {}", token);
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
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

}