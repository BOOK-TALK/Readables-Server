package com.book.backend.domain.auth.controller;

import com.book.backend.domain.auth.dto.KakaoUserInfoDto;
import com.book.backend.domain.auth.dto.LoginDto;
import com.book.backend.domain.auth.dto.SignupDto;
import com.book.backend.domain.auth.service.AuthService;
import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.global.log.RequestLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody SignupDto signupDto) {
        RequestLogger.body(signupDto);

        UserDto userDto = authService.signup(signupDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(HttpServletRequest request, @Valid @RequestBody LoginDto loginDto) {
        RequestLogger.body(loginDto);

        UserDto userDto = authService.login(request, loginDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        RequestLogger.param(new String[] {"Session ID"}, request.getSession().getId());

        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount(HttpServletRequest request) {
        RequestLogger.param(new String[] {"Session ID"}, request.getSession().getId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        authService.deleteAccountByLoginId(loginId);
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
