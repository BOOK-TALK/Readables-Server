package com.book.backend.domain.openapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TestDto implements OpenAPIDtoInterface {
    @NotBlank(message = "pageSize 필수 입력값입니다.")
    private int pageSize;
}
