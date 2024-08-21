package com.book.backend.domain.user.entity;

import com.book.backend.domain.auth.entity.RefreshToken;
import com.book.backend.domain.user.dto.LibraryDto;
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

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "userOpentalkId", fetch = FetchType.LAZY)
    private List<UserOpentalk> openTalkIds;  // 즐찾 오픈톡

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_libraries", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "library")
    private List<LibraryDto> libraries;
}
