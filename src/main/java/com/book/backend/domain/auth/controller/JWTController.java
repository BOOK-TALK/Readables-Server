package com.book.backend.domain.auth.controller;

import com.book.backend.domain.auth.dto.JwtTokenDto;
import com.book.backend.domain.auth.dto.LoginDto;
import com.book.backend.domain.auth.dto.LoginSuccessResponse;
import com.book.backend.domain.auth.service.CustomUserDetailsService;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.util.JwtUtil;
import io.swagger.v3.oas.models.links.Link;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

@Slf4j
@RequiredArgsConstructor
@RestController
public class JwtController {
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager; // authenticate 메서드 : username, password 기반 인증 수행
    private final ResponseTemplate responseTemplate;

    @PostMapping("/api/auth/jwt")
    public ResponseEntity<?> authenticateTest(@RequestBody LoginDto dto) {
        log.info("/auth 호출");
        try {
            // 인증 시도
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("로그인 실패");
        }
        // 인증 성공 후 인증된 user의 정보를 갖고 옴
        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getLoginId());
        // subject, claim 모두 UserDetails를 사용하므로 객체를 그대로 전달
        JwtTokenDto jwtTokenDto = jwtUtil.generateToken(userDetails);

        return responseTemplate.success(jwtTokenDto, HttpStatus.OK);
    }
}
