package com.book.backend.domain.openapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendListResponseDto implements OpenAPIResponseInterface {
    private String bookname;
    private String authors;
    private String publisher;
    private String publication_year;
    private String isbn13;
    private String vol;
    private String class_no;
    private String class_nm;
    private String bookImageURL;
    
    public static final String description = "bookname: 책이름 | authors: 작가 옮긴이 | publisher: 출판사 | "
            + "publication_year: 출판년도 | isbn13: 책 바코드 | additional_symbol: 책 바코드 부가기호? | vol: 권? | "
            + "class_no: 주제코드 | class_nm: 주제분류 | bookImageURL: 책표지 URL";
}

