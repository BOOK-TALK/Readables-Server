package com.book.backend.domain.openapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ManiaRequestDto implements OpenAPIRequestInterface {
    @NotBlank(message = "isbn13은 필수 입력값입니다.")
    private String isbn13;

    @Builder.Default
    private final String type = "mania";
}
