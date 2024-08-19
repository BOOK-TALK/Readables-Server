package com.book.backend.domain.auth.dto;

import jakarta.validation.constraints.Email;
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
public class SignupDto {
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String loginId;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String nickname;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 숫자, 영문자, 특수문자를 포함한 8~16자리여야 합니다.")
    private String password;

    @NotBlank(message = "성별은 필수 입력값입니다.")
    @Pattern(regexp = "NOT_SELECTED|MAN|WOMAN", message = "성별은 NOT_SELECTED, MAN, WOMAN 중 하나여야 합니다.")
    private String gender;

    @Past(message = "현재 날짜보다 이전 날짜여야 합니다.")
    private LocalDate birthDate;

    // FIXME: 아래 필드는 삭제 예정
    @Email(message = "유효한 이메일 주소여야 합니다.")
    private String email;

    @Pattern(regexp = "010-\\d{4}-\\d{4}", message = "전화번호는 010-XXXX-XXXX 형태여야 합니다.")
    private String phone;

}
