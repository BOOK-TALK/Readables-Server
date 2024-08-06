package com.book.backend.domain.openapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotTrendResponseDto implements OpenAPIResponseInterface {
    private String no;
    private String difference;
    private String baseWeekRank;
    private String pastWeekRank;
    private String bookname;
    private String authors;
    private String publisher;
    private String publication_year;
    private String isbn13;
    private String addition_symbol;
    private String vol;
    private String class_no;
    private String class_nm;
    private String bookImageURL;
    private String bookDtlUrl;
}
