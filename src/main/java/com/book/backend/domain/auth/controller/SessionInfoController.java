package com.book.backend.domain.auth.controller;

import com.book.backend.global.log.RequestLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/session-info")
@RequiredArgsConstructor
@Slf4j
public class SessionInfoController {

    @GetMapping
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "세션이 없습니다.";
        }

        // 세션에 저장된 모든 속성의 이름을 가져와 로그에 출력
        session.getAttributeNames().asIterator().forEachRemaining(name ->
                log.info("session name={}, value={}", name, session.getAttribute(name))
        );

        // Spring Security Context에서 인증 정보 불러와서 출력
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User user) {
                log.info("username={}", user.getUsername());
                log.info("authorities={}", user.getAuthorities());
            } else {
                log.info("principal={}", principal);
            }
        } else {
            log.info("사용자가 인증되지 않았습니다.");
        }

        // 세션 정보를 로그에 출력
        RequestLogger.param(new String[] {"sessionId", "maxInactiveInterval", "creationTime", "lastAccessedTime", "isNew"},
                session.getId(),
                session.getMaxInactiveInterval(),
                new Date(session.getCreationTime()),
                new Date(session.getLastAccessedTime()),
                session.isNew());

        return "세션 정보가 서버 로그에 출력되었습니다.";
    }

}
