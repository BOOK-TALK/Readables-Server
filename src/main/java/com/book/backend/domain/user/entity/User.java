package com.book.backend.domain.user.entity;

import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.user.dto.LibraryDto;
import com.book.backend.domain.userBook.dto.UserBookDto;
import com.book.backend.domain.userOpentalk.entity.UserOpentalk;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

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

    @Column(unique = true)
    private String appleId;

    private LocalDateTime regDate;

    @Column(unique = true)
    private String nickname;

    private String password;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    private LocalDate birthDate;

    private String profileImageUrl;

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserOpentalk> openTalkIds;  // 즐찾 오픈톡

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;  // 작성한 메시지

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_libraries", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "library")
    private List<LibraryDto> libraries;  // 저장한 도서관

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user dibs_books", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "isbn")
    private List<UserBookDto> dibsBooks;  // 책 찜

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_read_books", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "isbn")
    private List<UserBookDto> readBooks;  // 읽은 책
}
