package com.book.backend.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class JwtTokenDto {
    String accessToken;
    String refreshToken;

    public static final String description =
            "accessToken : 액세스 토큰 | " +
            "refreshToken : 리프레쉬 토큰";
}
