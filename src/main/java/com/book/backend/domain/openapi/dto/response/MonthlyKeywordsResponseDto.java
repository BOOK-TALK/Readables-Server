package com.book.backend.domain.openapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyKeywordsResponseDto implements OpenAPIResponseInterface{
    private String keyword;
    private String weight; // 중요도

    public static final String description = "keyword: 검색어 | weight: 중요도";
}
