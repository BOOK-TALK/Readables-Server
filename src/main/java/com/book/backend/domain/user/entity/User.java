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

    private LocalDateTime regDate;

    @Column(unique = true)
    private String loginId;

    private String nickname;

    private String password;

    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

    private LocalDate birthDate;

    private String email;

    private String phone;

}
