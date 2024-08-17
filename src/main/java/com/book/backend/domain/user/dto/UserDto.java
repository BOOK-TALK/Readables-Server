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

    private LocalDateTime regDate;

    private String loginId;

    private String password;

    private String gender;

    private LocalDate birthDate;

    private String email;

    private String phone;

    public static final String description =
            "userId : 유저 아이디 | " +
            "kakaoId : 유저 카카오 아이디 (카카오 로그인 시 생성) | " +
            "regDate : 가입 시각 | " +
            "loginId : 유저 로그인 아이디 (일반 로그인 시 생성) | " +
            "password : 암호화된 비밀번호 (일반 로그인 시 생성) | " +
            "gender : 성별 | " +
            "birthDate : 생일 | " +
            "email : 이메일 주소 | " +
            "phone : 휴대전화 번호";
}
