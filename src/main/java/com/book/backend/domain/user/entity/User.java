package com.book.backend.domain.user.entity;

import com.book.backend.domain.userOpentalk.entity.UserOpentalk;
import jakarta.persistence.*;
import java.util.List;
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
    private Gender gender;

    private LocalDate birthDate;

    private String email;

    private String phone;

    @OneToMany(mappedBy = "userOpentalkId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserOpentalk> openTalkIds; //즐찾 오픈톡
}
