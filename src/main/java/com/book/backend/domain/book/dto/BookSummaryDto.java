package com.book.backend.domain.book.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSummaryDto {
    private String isbn;
    private String title;
    private String author;
    private String imageUrl;
}
