package com.book.backend.domain.auth.service;

import com.book.backend.domain.auth.dto.JwtTokenDto;
import com.book.backend.domain.auth.dto.LoginSuccessResponseDto;
import com.book.backend.domain.oidc.OidcProviderFactory;
import com.book.backend.domain.oidc.Provider;
import com.book.backend.domain.user.entity.Gender;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
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
public class OAuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final OidcProviderFactory oidcProviderFactory;

    // 카카오 로그인
    @Transactional
    public LoginSuccessResponseDto oAuthLogin(Provider provider, String idToken) {
        log.trace("OAuthService > oAuthLogin()");

        if (idToken == null || idToken.isEmpty()){
            throw new CustomException(ErrorCode.ID_TOKEN_IS_NULL);
        }

        String providerId = oidcProviderFactory.getProviderId(provider, idToken);

        // kakaoId로 유저 조회
        User user = userService.findByUsername(providerId);
        Boolean isNewUser = Boolean.FALSE;

        // 조회된 유저가 없을 시 회원가입 처리
        if (user == null) {
            isNewUser = Boolean.TRUE;
            User newUser = new User();
            if (provider == Provider.KAKAO) {  // 카카오 로그인인 경우
                newUser.setKakaoId(providerId);
            } else {  // 애플 로그인인 경우
                newUser.setAppleId(providerId);
            }
            newUser.setRegDate(LocalDateTime.now());
            newUser.setNickname("");  // 빈 문자열로 설정
            newUser.setGender(Gender.G0);
            user = newUser;
        }

        userRepository.save(user);

        // UserDetailsService를 사용하여 UserDetails 객체 생성
        UserDetails userDetails = userDetailsService.loadUserByUsername(providerId);
        JwtTokenDto jwtTokenDto = jwtUtil.generateToken(userDetails);

        // 사용자 인증 정보 생성 및 SecurityContext에 저장
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Redis에 RefreshToken 저장
        jwtUtil.storeRefreshTokenInRedis(authentication, jwtTokenDto.getRefreshToken());

        return LoginSuccessResponseDto.builder()
                .userId(user.getUserId())
                .isNewUser(isNewUser)
                .accessToken(jwtTokenDto.getAccessToken())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .build();
    }
}
