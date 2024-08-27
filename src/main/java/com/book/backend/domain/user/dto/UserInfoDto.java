package com.book.backend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoDto {
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String nickname;

    private String gender;

    private LocalDate birthDate;

    private String profileImageUrl; // TODO : 구현

    public static final String description =
            "nickname : 닉네임 | " +
            "gender : 성별 | " +
            "birthDate : 생일" +
            "profileImageUrl : 프로필 이미지 URL (지금은 null)";
}
