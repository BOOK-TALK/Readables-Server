package com.book.backend.domain.book.mapper;

import com.book.backend.domain.book.dto.BookSummaryDto;
import com.book.backend.domain.book.dto.BookInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {

    public BookSummaryDto convertToBookSummaryDto(BookInfoDto bookInfoDto) {
        return BookSummaryDto.builder()
                .isbn(bookInfoDto.getIsbn13())
                .title(bookInfoDto.getBookname())
                .author(bookInfoDto.getAuthors())
                .imageUrl(bookInfoDto.getBookImageURL())
                .build();
    }
}
