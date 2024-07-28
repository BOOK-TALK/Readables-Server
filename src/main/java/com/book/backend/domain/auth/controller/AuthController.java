package com.book.backend.domain.auth.controller;

import com.book.backend.domain.auth.dto.SignupDto;
import com.book.backend.domain.auth.service.AuthService;
import com.book.backend.domain.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody SignupDto signupDto) {
        UserDto userDto = authService.signup(signupDto);
        return ResponseEntity.ok(userDto);
    }
}
