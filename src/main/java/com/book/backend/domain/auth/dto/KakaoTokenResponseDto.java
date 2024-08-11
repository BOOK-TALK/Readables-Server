package com.book.backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoTokenResponseDto {
    @JsonProperty("access_token")
    private String accessToken;

    // 아래의 프로퍼티들은 필요시 추가
//    @JsonProperty("token_type")
//    private String tokenType;
//
//    @JsonProperty("refresh_token")
//    private String refreshToken;
//
//    @JsonProperty("id_token")
//    private String idToken;
//
//    @JsonProperty("expires_in")
//    private String expiresIn;
//
//    @JsonProperty("scope")
//    private String scope;
//
//    @JsonProperty("refresh_token_expires_in")
//    private String refreshTokenExpiresIn;

}
