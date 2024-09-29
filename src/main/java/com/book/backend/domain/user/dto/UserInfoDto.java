package com.book.backend.domain.user.dto;

import com.book.backend.domain.user.validator.ValidNickname;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoDto {
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @ValidNickname
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
