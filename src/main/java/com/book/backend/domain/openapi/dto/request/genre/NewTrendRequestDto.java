package com.book.backend.domain.openapi.dto.request.genre;


import com.book.backend.domain.openapi.dto.request.OpenAPIRequestInterface;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewTrendRequestDto implements OpenAPIRequestInterface {
    @NotBlank(message = "subKdc는 필수 입력값입니다.")
    private String dtl_kdc;
    private Integer pageSize;  // 페이지 사이즈
}
