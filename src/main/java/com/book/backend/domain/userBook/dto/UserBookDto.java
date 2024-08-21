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
    private String bookname;
    private String bookImageURL;
    private String isbn;
}
