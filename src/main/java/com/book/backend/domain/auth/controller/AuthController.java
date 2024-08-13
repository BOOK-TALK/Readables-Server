package com.book.backend.domain.auth.controller;

import com.book.backend.domain.auth.dto.LoginDto;
import com.book.backend.domain.auth.dto.LoginSuccessResponseDto;
import com.book.backend.domain.auth.dto.SignupDto;
import com.book.backend.domain.auth.service.AuthService;
import com.book.backend.domain.auth.service.KakaoService;
import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final KakaoService kakaoService;
    private final ResponseTemplate responseTemplate;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody SignupDto signupDto) {
        RequestLogger.body(signupDto);

        UserDto userDto = authService.signup(signupDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        RequestLogger.body(loginDto);

        LoginSuccessResponseDto loginSuccessResponseDto = authService.login(loginDto);
        return ResponseEntity.ok(loginSuccessResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        RequestLogger.param(new String[] {"Session ID"}, request.getSession().getId());

//        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount(HttpServletRequest request) {
        RequestLogger.param(new String[] {"Session ID"}, request.getSession().getId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        authService.deleteAccountByLoginId(loginId);
//        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "카카오 로그인", description = "사용자가 카카오 인증 서버에서 받은 인가 코드를 parameter로 받아 카카오계정으로 로그인을 진행하고, 완료된 유저 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "authorizationCode", description = "인가 코드")
            },
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class)),
                    description = UserDto.description)})
    @PostMapping("/kakaoLogin")
    public ResponseEntity<?> kakaoLogin(HttpServletRequest request, String authorizationCode) {
        RequestLogger.param(new String[] {"Session Id"}, request.getSession().getId());

        UserDto userDto = kakaoService.kakaoLogin(request, authorizationCode);

        return responseTemplate.success(userDto, HttpStatus.OK);
    }
}
