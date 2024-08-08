package com.book.backend.domain.openapi.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanItemSrchResponseDto implements OpenAPIResponseInterface {
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

    public static final String description = "no: 순번 | ranking: 순위 | bookname: 책이름 | authors: 작가, 옮긴이 | publisher: 출판사 | "
            + "publication_year: 출판년도 | isbn13: 책 바코드 | additional_symbol: 책 바코드 부가기호? | vol: 권? | class_no: 주제코드 | "
            + "class_nm: 주제분류 | loan_count: 대출횟수 | bookImageURL: 책표지 URL | bookDtlUrl: 책 상세정보 URL";
}
