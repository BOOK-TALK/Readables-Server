package com.book.backend.domain.openapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HotTrendRequestDto implements OpenAPIRequestInterface {
    @NotBlank(message = "searchDt 은 필수 입력값입니다. (yyyy-MM-dd)")
    private String searchDt;
}
