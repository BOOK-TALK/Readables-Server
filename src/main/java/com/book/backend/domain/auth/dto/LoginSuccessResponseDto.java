package com.book.backend.domain.auth.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessResponseDto {
    private Long userId;
    private String accessToken;
    private String refreshToken;

    public static final String description =
            "userId : 유저 아이디 | " +
            "accessToken : 액세스 토큰 | " +
            "refreshToken : 리프레쉬 토큰";
}
