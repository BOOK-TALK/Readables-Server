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
    private List<UserBookDto> dibsBooks; // 찜한 책
    private List<UserBookDto> readBooks; // 읽은 책

    public static final String description =
            "userDto : 유저 정보 | " +
            "libraries : 도서관 목록 | " +
            "dibsBooks : 찜한 책 목록 |" +
            "readBooks : 읽은 책 목록";
}
