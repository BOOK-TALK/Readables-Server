package com.book.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String loginId;

    @Column(unique = true)
    private String kakaoId;

    private LocalDateTime regDate;

    private String nickname;

    private String password;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    private LocalDate birthDate;

    private String email;

    private String phone;

}
