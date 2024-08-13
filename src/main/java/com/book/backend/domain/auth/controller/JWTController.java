package com.book.backend.domain.auth.controller;

import com.book.backend.domain.auth.dto.LoginDto;
import com.book.backend.domain.auth.dto.LoginSuccessResponse;
import com.book.backend.domain.auth.service.CustomUserDetailsService;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class JWTController {
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager; // authenticate 메서드 : username, password 기반 인증 수행

    @PostMapping("/api/auth/jwt")
    public ResponseEntity<LoginSuccessResponse> authenticateTest(@RequestBody LoginDto dto) {
        log.info("/auth 호출");
        try {
            // 인증 시도
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("로그인 실패");
        }
        // 인증 성공 후 인증된 user의 정보를 갖고옴
        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getLoginId());
        // subject, claim 모두 UserDetails를 사용하므로 객체를 그대로 전달
        String token = jwtUtil.generateToken(userDetails);

        // 생성된 토큰을 응답 (Test)
        return ResponseEntity.ok(new LoginSuccessResponse(token));
    }
}
