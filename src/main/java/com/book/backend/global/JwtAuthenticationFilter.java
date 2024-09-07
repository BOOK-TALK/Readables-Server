package com.book.backend.global;

import com.book.backend.domain.auth.service.CustomUserDetailsService;
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
        if (isSwaggerRequest(requestURI)) {
            filterChain.doFilter(wrappedRequest, response);
            return;
        }

        // 요청 헤더에서 Authorization 추출
        String authorization = wrappedRequest.getHeader("Authorization");
        if (authorization == null) {
            setJwtException(request, ErrorCode.JWT_NOT_FOUND);

            filterChain.doFilter(wrappedRequest, response);
            return;
        }

        processAuthorizationToken(authorization, wrappedRequest, response, filterChain);
    }

    private boolean isSwaggerRequest(String requestURI) {
        return requestURI.startsWith("/swagger-ui/") || requestURI.startsWith("/v3/api-docs");
    }

    private void processAuthorizationToken(String authorization, RequestWrapper wrappedRequest, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            if (!authorization.startsWith("Bearer ")) {
                throw new CustomException(ErrorCode.JWT_NOT_FOUND);
            }

            String token = authorization.substring(7);

            // 블랙리스트에 있는 토큰인지 검증
            if (jwtUtil.isBlacklisted(token)) {
                throw new CustomException(ErrorCode.JWT_IS_BLACKLISTED);
            }

            String username = jwtUtil.getUsernameFromToken(token);

            // 현재 SecurityContextHolder에 인증객체가 있는지 확인
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateUser(username, token, wrappedRequest, response);
            }
        } catch (ExpiredJwtException e) {
            setJwtException(wrappedRequest, ErrorCode.JWT_EXPIRED);
        } catch (CustomException e) {
            setJwtException(wrappedRequest, e.getCode());
        } catch (Exception e) {
            setJwtException(wrappedRequest, ErrorCode.INVALID_CREDENTIALS);
        } finally {
            filterChain.doFilter(wrappedRequest, response);
        }
    }

    private void authenticateUser(String username, String token, RequestWrapper request, HttpServletResponse response) throws IOException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 토큰 유효성 검증
        if (!jwtUtil.isValidToken(token, userDetails)) {
            return;
        }

        // SecurityContextHolder에 인증객체 추가
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰 갱신
        String newAccessToken = jwtUtil.generateToken(userDetails).getAccessToken();
        response.setHeader("Authorization", "Bearer " + newAccessToken);
    }

    private void setJwtException(HttpServletRequest request, ErrorCode errorCode) {
        request.setAttribute("JWTException", new CustomException(errorCode));
    }
}
