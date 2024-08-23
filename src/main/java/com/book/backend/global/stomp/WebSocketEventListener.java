package com.book.backend.global.stomp;

import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import com.book.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map header = (Map)((GenericMessage) accessor.getHeader("simpConnectMessage"))
                .getHeaders().get("nativeHeaders");

        if (header.containsKey("Authorization")){
            String token = (String) ((List) header.get("Authorization")).get(0);
            if(token.startsWith("Bearer ")){
                token = token.substring(7);
                vaildateToken(accessor, token); //토큰 검증
            } else {
                throw new RuntimeException("JWT 토큰 형식이 잘못 됐습니다.");
            }
        }
    }

    public void vaildateToken(StompHeaderAccessor accessor, String token){
       String username = jwtUtil.getUsernameFromToken(token);  // username 가져옴

        // 현재 SecurityContextHolder에 인증객체가 있는지 확인
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            try {
                userDetails = userDetailsService.loadUserByUsername(username);
            } catch (CustomException e) {
                throw new CustomException(ErrorCode.JWT_NOT_FOUND);
            }

            // 토큰 유효성 검증
            if (jwtUtil.isValidToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticated
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

//                // wrappedRequest 대신에 accessor 로 HttpServletRequest 를 대신할 수 없을까
                authenticated.setDetails((new WebAuthenticationDetailsSource()).buildDetails(
                        (HttpServletRequest)((GenericMessage) accessor.getMessageHeaders().get("simpConnectMessage")).getHeaders().get("nativeHeaders")
                ));
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            }
        } else {
            throw new CustomException(ErrorCode.INVALID_WEEK_MONTH);
        }
    }
}
