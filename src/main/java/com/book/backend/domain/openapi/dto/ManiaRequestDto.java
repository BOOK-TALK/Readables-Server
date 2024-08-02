package com.book.backend.domain.openapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ManiaRequestDto implements OpenAPIDtoInterface {
    @NotBlank(message = "isbn13은 필수 입력값입니다.")
    private String isbn13;

    @Builder.Default
    private final String type = "mania";

    @Builder.Default
    private String format = "json"; // or xml
}
