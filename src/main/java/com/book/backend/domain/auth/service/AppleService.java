package com.book.backend.domain.auth.service;

import com.book.backend.domain.auth.dto.JwtTokenDto;
import com.book.backend.domain.auth.dto.LoginSuccessResponseDto;
import com.book.backend.domain.oidc.OidcProviderFactory;
import com.book.backend.domain.oidc.Provider;
import com.book.backend.domain.user.entity.Gender;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AppleService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final OidcProviderFactory oidcProviderFactory;
    private final JwtUtil jwtUtil;
    private final JwtRefreshTokenService jwtRefreshTokenService;

    // TODO: kakaoLogin과 중복되는 코드 리팩토링 필요
    @Transactional
    public LoginSuccessResponseDto appleLogin(String idToken) {
        log.trace("AppleService > appleLogin()");

        String providerId = oidcProviderFactory.getProviderId(Provider.APPLE, idToken);

        // appleId로 유저 조회
        User user = userService.findByUsername(providerId);
        Boolean isNewUser = Boolean.FALSE;

        // 조회된 유저가 없을 시 회원가입 처리
        if (user == null) {
            isNewUser = Boolean.TRUE;
            User newUser = new User();
            newUser.setAppleId(providerId);
            newUser.setRegDate(LocalDateTime.now());
            newUser.setPassword("unused");  // 애플 로그인 사용자는 패스워드 사용하지 않음
            newUser.setLoginId(null);  // 애플 로그인 사용자는 loginId가 null
            newUser.setNickname("");  // 빈 문자열로 설정
            newUser.setGender(Gender.G0);
            user = newUser;
        }

        userRepository.save(user);

        // UserDetailsService를 사용하여 UserDetails 객체 생성
        UserDetails userDetails = userDetailsService.loadUserByAppleId(providerId);
        JwtTokenDto jwtTokenDto = jwtUtil.generateToken(userDetails);

        // 사용자 인증 정보 생성 및 SecurityContext에 저장
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Redis에 RefreshToken 저장
        jwtUtil.storeRefreshTokenInRedis(authentication, jwtTokenDto.getRefreshToken());

        // FIXME: 수정 필요
        // Refresh Token 갱신
        jwtRefreshTokenService.updateRefreshToken(jwtTokenDto, user);

        return LoginSuccessResponseDto.builder()
                .userId(user.getUserId())
                .isNewUser(isNewUser)
                .accessToken(jwtTokenDto.getAccessToken())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .build();
    }
}
