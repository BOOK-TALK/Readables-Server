package com.book.backend.domain.openapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailRequestDto implements OpenAPIRequestInterface {
    private String isbn13;
}
