package com.book.backend.domain.openapi.dto.response.genre;

import com.book.backend.domain.openapi.dto.response.OpenAPIResponseInterface;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodTrendResponseDto implements OpenAPIResponseInterface {
    private String no;
    private String ranking;
    private String bookname;
    private String authors;
    private String publisher;
    private String publication_year;
    private String isbn13;
    private String addition_symbol;
    private String class_no;
    private String class_nm;
    private String loan_count;
    private String bookImageURL;
    private String bookDtlUrl;
}
