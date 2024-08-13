package com.book.backend.domain.auth.service;

import com.book.backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // Swagger 경로에 대한 요청인 경우 필터링 과정 건너뛰기
        if (requestURI.startsWith("/swagger-ui/") || requestURI.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 요청 헤더에서 Authorization 요소 추출
        String authorization = request.getHeader("Authorization");
        String username = "", token = "";

        if (authorization != null && authorization.startsWith("Bearer ")) {  // Bearer 토큰 파싱
            token = authorization.substring(7);  // jwt token 파싱
            username = jwtUtil.getUsernameFromToken(token);  // username 가져옴
        } else {
            filterChain.doFilter(request, response);
        }

        // 현재 SecurityContextHolder에 인증객체가 있는지 확인
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 토큰 유효성 검증
            log.info("JWT Filter token = {}", token);
            log.info("JWT Filter userDetails = {}", userDetails.getUsername());

            if (jwtUtil.isValidToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 인증 토큰에 요청 세부 정보 설정
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Security Context에 인증 설정
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}