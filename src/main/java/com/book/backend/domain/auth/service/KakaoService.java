package com.book.backend.domain.auth.service;

import com.book.backend.domain.auth.dto.JwtTokenDto;
import com.book.backend.domain.auth.dto.KakaoTokenResponseDto;
import com.book.backend.domain.auth.dto.KakaoUserInfoDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class KakaoService {
    @Value("${kakao.restapiKey}")
    private String restApiKey;
    @Value("${kakao.redirectUri}")
    private String redirectUri;
    @Value("${kakao.tokenRequestUri}")
    private String tokenRequestUri;
    @Value("${kakao.userInfoUri}")
    private String userInfoUri;

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtRefreshTokenService jwtRefreshTokenService;
    private final OidcProviderFactory oidcProviderFactory;

    // Redirect URI에 전달된 코드값으로 Access Token 요청
    public KakaoTokenResponseDto getAccessToken(String authorizationCode) {
        log.trace("KakaoService > getAccessToken()");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", restApiKey);
            body.add("redirect_uri", redirectUri);
            body.add("code", authorizationCode);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            return restTemplate.postForEntity(tokenRequestUri, request, KakaoTokenResponseDto.class).getBody();
        } catch (HttpClientErrorException e) {
            log.trace("HttpClientErrorException: Status code: {}, Response body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        } catch (HttpServerErrorException e) {
            throw new CustomException(ErrorCode.KAKAO_SERVER_ERROR);
        }
    }

    // Access Token으로 유저 정보 요청
    public KakaoUserInfoDto getUserInfo(String accessToken) {
        log.trace("KakaoService > getUserInfo()");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBearerAuth(accessToken);

            HttpEntity<String> request = new HttpEntity<>(headers);

            // 일단 id만 받아오도록 구현, 향후 필요 시 유저 프로필 정보 가져오도록 구현 가능
            ResponseEntity<KakaoUserInfoDto> response = restTemplate.exchange(
                    userInfoUri, HttpMethod.GET, request, KakaoUserInfoDto.class);
            return response.getBody();

        } catch (HttpClientErrorException e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        } catch (HttpServerErrorException e) {
            throw new CustomException(ErrorCode.KAKAO_SERVER_ERROR);
        }
    }

    // TODO: appleLogin과 중복되는 코드 리팩토링 필요
    // 카카오 로그인
    @Transactional
    public LoginSuccessResponseDto kakaoLogin(String idToken) {
        log.trace("KakaoService > kakaoLogin()");

        if (idToken == null || idToken.isEmpty()){
            throw new CustomException(ErrorCode.ID_TOKEN_IS_NULL);
        }

        String providerId = oidcProviderFactory.getProviderId(Provider.KAKAO, idToken);

        // kakaoId로 유저 조회
        User user = userService.findByUsername(providerId);
        Boolean isNewUser = Boolean.FALSE;

        // 조회된 유저가 없을 시 회원가입 처리
        if (user == null) {
            isNewUser = Boolean.TRUE;
            User newUser = new User();
            newUser.setKakaoId(providerId);
            newUser.setRegDate(LocalDateTime.now());
            newUser.setPassword("unused");  // 카카오 로그인 사용자는 패스워드 사용하지 않음
            newUser.setLoginId(null);  // 카카오 로그인 사용자는 loginId가 null
            newUser.setNickname("");  // 빈 문자열로 설정
            user = newUser;
        }

        userRepository.save(user);

        // UserDetailsService를 사용하여 UserDetails 객체 생성
        UserDetails userDetails = userDetailsService.loadUserByKakaoId(providerId);
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
