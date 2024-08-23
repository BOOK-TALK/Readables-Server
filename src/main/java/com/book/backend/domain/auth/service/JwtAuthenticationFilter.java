package com.book.backend.domain.auth.service;

import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import com.book.backend.util.JwtUtil;
import com.book.backend.util.RequestWrapper;
import io.jsonwebtoken.ExpiredJwtException;
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
        log.trace("JwtAuthenticationFilter > doFilterInternal()");

        RequestWrapper wrappedRequest = new RequestWrapper(request);

        String requestURI = wrappedRequest.getRequestURI();

        // Swagger 경로에 대한 요청인 경우 필터링 과정 건너뛰기
        if (requestURI.startsWith("/swagger-ui/") || requestURI.startsWith("/v3/api-docs")) {
            filterChain.doFilter(wrappedRequest, response);
            return;
        }

        // 요청 헤더에서 Authorization 요소 추출
        String authorization = wrappedRequest.getHeader("Authorization");
        String username = "", token = "";

        try {
            if (authorization != null && authorization.startsWith("Bearer ")) {  // Bearer 토큰 파싱
                token = authorization.substring(7);  // jwt token 파싱
                username = jwtUtil.getUsernameFromToken(token);  // username 가져옴

                // 현재 SecurityContextHolder에 인증객체가 있는지 확인
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails;

                    // TODO: 리팩토링 필요
                    try {
                        userDetails = userDetailsService.loadUserByloginId(username);
                    } catch (CustomException e1) {
                        try {
                            userDetails = userDetailsService.loadUserByKakaoId(username);
                        } catch (CustomException e2) {
                            userDetails = userDetailsService.loadUserByAppleId(username);
                        }
                    }

                    // 토큰 유효성 검증
                    if (jwtUtil.isValidToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticated
                                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authenticated.setDetails(new WebAuthenticationDetailsSource().buildDetails(wrappedRequest));
                        SecurityContextHolder.getContext().setAuthentication(authenticated);

                        // 토큰 갱신
                        String newAccessToken = jwtUtil.generateToken(userDetails).getAccessToken();
                        response.setHeader("Authorization", "Bearer " + newAccessToken);
                    }
                }
            } else {
                request.setAttribute("JWTException", new CustomException(ErrorCode.JWT_NOT_FOUND));
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("JWTException", new CustomException(ErrorCode.JWT_EXPIRED));
        } catch (Exception e) {
            request.setAttribute("JWTException", new CustomException(ErrorCode.INVALID_CREDENTIALS));
        }

        filterChain.doFilter(wrappedRequest, response);
    }
}
