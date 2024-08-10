package com.book.backend.domain.auth.service;

import com.book.backend.domain.auth.dto.KakaoTokenResponseDto;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.GRANT_TYPE;


@Service
@RequiredArgsConstructor
public class KakaoService {
    @Value("${kakao.restapiKey}")
    private String restApiKey;
    @Value("${kakao.redirectUri}")
    private String redirectUri;
    @Value("${kakao.tokenRequestUri}")
    private String tokenRequestUri;

    private final RestTemplate restTemplate;

    // Redirect URI에 전달된 코드값으로 Access Token 요청
    public KakaoTokenResponseDto getAccessToken(String authorizationCode) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", GRANT_TYPE);
            body.add("client_id", restApiKey);
            body.add("redirect_uri", redirectUri);
            body.add("code", authorizationCode);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            return restTemplate.postForEntity(tokenRequestUri, request, KakaoTokenResponseDto.class).getBody();
        } catch (HttpClientErrorException e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        } catch (HttpServerErrorException e) {
            throw new CustomException(ErrorCode.KAKAO_SERVER_ERROR);
        }
    }

}
