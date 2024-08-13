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
}
