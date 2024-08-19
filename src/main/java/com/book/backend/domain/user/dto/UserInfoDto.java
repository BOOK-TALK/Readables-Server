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

    @NotBlank(message = "성별은 필수 입력값입니다.")
    @Pattern(regexp = "NOT_SELECTED|MAN|WOMAN", message = "성별은 NOT_SELECTED, MAN, WOMAN 중 하나여야 합니다.")
    private String gender;

    @Past(message = "현재 날짜보다 이전 날짜여야 합니다.")
    private LocalDate birthDate;

    public static final String description =
            "nickname : 닉네임 | " +
            "gender : 성별 | " +
            "birthDate : 생일";
}
