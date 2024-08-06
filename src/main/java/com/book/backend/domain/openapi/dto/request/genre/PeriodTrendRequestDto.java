package com.book.backend.domain.openapi.dto.request.genre;

import com.book.backend.domain.openapi.dto.request.OpenAPIRequestInterface;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PeriodTrendRequestDto implements OpenAPIRequestInterface {
    @NotBlank(message = "subKdc는 필수 입력값입니다.")
    private String dtl_kdc;
    private String startDt;  // 검색시작일자
    private String endDt;  // 검색종료일자
}
