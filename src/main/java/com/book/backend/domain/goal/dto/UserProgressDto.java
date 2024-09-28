package com.book.backend.domain.goal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProgressDto {
    private String nickname;
    private Double progressRate;
    private String profileImageUrl;

    public static final String description =
            "nickname : 유저 닉네임 | " +
            "progressRate : 목표 진행률";

}
