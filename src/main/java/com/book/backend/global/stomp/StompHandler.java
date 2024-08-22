package com.book.backend.global.stomp;

import com.book.backend.domain.auth.service.JwtAuthenticationFilter;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import com.book.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 메세지에서 헤더 추출
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        // 초기 Request URL 로 연결 요청시에 실행
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            // JWT 토큰 추출
            String authorization = headerAccessor.getFirstNativeHeader("Authorization");
            String username = "", token = "";
            try {
                if (authorization == null) throw new IllegalArgumentException("Header 에 Authorization 토큰을 추가해주세요.");
                if (authorization.startsWith("Bearer ")) {  // Bearer 토큰 파싱
                    token = authorization.substring(7);  // jwt token 파싱
                    username = jwtUtil.getUsernameFromToken(token);  // username 가져옴

                    // 현재 SecurityContextHolder에 인증객체가 있는지 확인
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        // 토큰 유효성 검증
                        if (jwtUtil.isValidToken(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authenticated
                                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

//                            authenticated.setDetails(new WebAuthenticationDetailsSource().buildDetails(wrappedRequest));
                            SecurityContextHolder.getContext().setAuthentication(authenticated);

                            // 토큰 갱신
                            String newAccessToken = jwtUtil.generateToken(userDetails).getAccessToken();
//                            response.setHeader("Authorization", "Bearer " + newAccessToken);
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Bearer 토큰을 입력해주세요.");
                }
            } catch (ExpiredJwtException e) {
                throw new CustomException(ErrorCode.JWT_EXPIRED);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
            }
        }
        return message;
    }
}
