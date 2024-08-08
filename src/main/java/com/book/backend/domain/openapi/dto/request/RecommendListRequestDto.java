package com.book.backend.domain.openapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RecommendListRequestDto implements OpenAPIRequestInterface {
    @NotBlank(message = "isbn13은 필수 입력값입니다.")
    private String isbn13;

    private String type; // mania, reader
}
