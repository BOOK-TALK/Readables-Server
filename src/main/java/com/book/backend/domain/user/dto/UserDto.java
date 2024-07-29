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

    private LocalDateTime regDate;

    private String loginId;

    private String password;

    private String gender;

    private LocalDate birthDate;

    private String email;

    private String phone;
}
