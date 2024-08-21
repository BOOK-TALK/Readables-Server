package com.book.backend.domain.userBook.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
@AllArgsConstructor
@Builder
public class UserBookDto {
    private String isbn;
    private String bookname;
    private String bookImageURL;

    public static final String description = "isbn: 책 ISBN | bookname: 책 이름 | bookImageURL: 책 이미지 URL";
}
