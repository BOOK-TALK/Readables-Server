package com.book.backend.domain.openapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendResponseDto implements OpenAPIResponseInterface {
    private String bookname;
    private String authors;
    private String publisher;
    private String publication_year;
    private String isbn13;
    private String additional_symbol;
    private String vol;
    private String class_no;
    private String class_nm;
    private String bookImageURL;
}

