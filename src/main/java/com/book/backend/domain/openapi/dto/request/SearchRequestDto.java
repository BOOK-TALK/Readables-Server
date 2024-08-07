package com.book.backend.domain.openapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchRequestDto implements OpenAPIRequestInterface {
    @NotBlank(message = "검색어를 입력해주세요.")
//    private String type; // name, author, keyword
    private String keyword; // 일단 책이름으로만 검색
    private int pageNo; // 페이지 번호
    private int pageSize; // 한 페이지에 담을 요소 수
}
