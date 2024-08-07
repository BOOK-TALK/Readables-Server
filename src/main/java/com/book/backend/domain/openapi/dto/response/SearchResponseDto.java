package com.book.backend.domain.openapi.dto.response;

import com.book.backend.domain.openapi.dto.request.OpenAPIRequestInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class SearchResponseDto implements OpenAPIResponseInterface, OpenAPIRequestInterface {
    private String bookname;
    private String authors;
    private String publisher;
    private String publication_year;
    private String isbn13;
    private String vol;
    private String bookImageURL;
    private String bookDtlUrl;
    private String loan_count;

    private boolean isLoanAvailable;
}
