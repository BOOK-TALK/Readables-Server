package com.book.backend.domain.user.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String kakaoId;
    private String appleId;
    private LocalDateTime regDate;
    private String nickname;
    private String gender;
    private LocalDate birthDate;
    private String profileImageUrl;

    public static final String description =
            "userId : 유저 아이디 | " +
            "kakaoId : 유저 카카오 아이디 (카카오 로그인 시 생성) | " +
            "appleId : 유저 애플 아이디 (애플 로그인 시 생성) | " +
            "regDate : 가입 시각 | " +
            "nickname : 닉네임 | " +
            "gender : 성별 | " +
            "birthDate : 생일" +
            "profileImageUrl : 프로필 이미지 URL (지금은 null)";
}
