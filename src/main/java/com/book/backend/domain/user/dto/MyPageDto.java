package com.book.backend.domain.user.dto;

import com.book.backend.domain.userBook.dto.UserBookDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MyPageDto {
    private UserDto userDto;
    private List<LibraryDto> libraries;
    private List<UserBookDto> dibs; // 찜한 책
    // TODO : 읽은 책 list

    public static final String description =
            "userDto : 유저 정보 | " +
            "libraries : 도서관 목록 | " +
            "dibs : 찜한 책 목록";
}
