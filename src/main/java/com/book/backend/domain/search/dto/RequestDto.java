package com.book.backend.domain.search.dto;

import com.book.backend.domain.openapi.dto.request.OpenAPIRequestInterface;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestDto implements OpenAPIRequestInterface {
    @NotBlank(message = "키워드 여부를 true/false 로 입력해주세요.")
    private boolean isKeyword;

    @NotBlank(message = "검색어를 입력해주세요.")
    private String input; // 검색어

    private int pageNo; // 페이지 번호
    private int pageSize; // 한 페이지에 담을 요소 수
}
