package com.book.backend.domain.auth.controller;

import com.book.backend.domain.auth.dto.LoginDto;
import com.book.backend.domain.auth.dto.LoginSuccessResponseDto;
import com.book.backend.domain.auth.dto.SignupDto;
import com.book.backend.domain.auth.service.AppleService;
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
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="유저 관리", description = "탈퇴 / 회원가입 / 로그인 / 카카오 로그인 / 애플 로그인")
public class AuthController {

    private final AuthService authService;
    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final ResponseTemplate responseTemplate;

    @Operation(summary = "회원가입", description = "기본 회원가입을 진행합니다.",
            responses = {@ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UserDto.class)),
                    description = UserDto.description)})
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDto) {
        RequestLogger.body(signupDto);

        UserDto userDto = authService.signup(signupDto);
        return responseTemplate.success(userDto, HttpStatus.CREATED);
    }

    @Operation(summary = "로그인", description = "기본 로그인을 진행합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginSuccessResponseDto.class)),
                    description = LoginSuccessResponseDto.description)})
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        RequestLogger.body(loginDto);

        LoginSuccessResponseDto loginSuccessResponseDto = authService.login(loginDto);
        return responseTemplate.success(loginSuccessResponseDto, HttpStatus.OK);
    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request) {
//        RequestLogger.param(new String[] {"Session ID"}, request.getSession().getId());
//
//        SecurityContextHolder.clearContext();
//        return ResponseEntity.ok("success");
//    }

    @Operation(summary = "탈퇴",
            responses = {@ApiResponse(responseCode = "204", description = "회원 탈퇴가 완료되었습니다.")})
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        authService.deleteAccountByUsername(username);
        SecurityContextHolder.clearContext();

        // TODO: 토큰 비활성화 처리 필요

        return responseTemplate.success("회원 탈퇴가 완료되었습니다.", HttpStatus.OK);
    }

    @Operation(summary = "카카오 로그인", description = "iOS SDK에서 발급된 id_token을 parameter로 받아 카카오 로그인을 진행하고, 완료된 유저 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "idToken", description = "id_token 값")
            },
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginSuccessResponseDto.class)),
                    description = LoginSuccessResponseDto.description)})
    @PostMapping("/kakaoLogin")
    public ResponseEntity<?> kakaoLogin(String idToken) {
        LoginSuccessResponseDto loginSuccessResponseDto = kakaoService.kakaoLogin(idToken);

        return responseTemplate.success(loginSuccessResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "애플 로그인", description = "iOS SDK에서 발급된 id_token을 parameter로 받아 애플 로그인을 진행하고, 완료된 유저 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "idToken", description = "id_token 값")
            },
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginSuccessResponseDto.class)),
                    description = LoginSuccessResponseDto.description)})
    @PostMapping("/appleLogin")
    public ResponseEntity<?> appleLogin(String idToken) {
        LoginSuccessResponseDto loginSuccessResponseDto = appleService.appleLogin(idToken);

        return responseTemplate.success(loginSuccessResponseDto, HttpStatus.OK);
    }
}
