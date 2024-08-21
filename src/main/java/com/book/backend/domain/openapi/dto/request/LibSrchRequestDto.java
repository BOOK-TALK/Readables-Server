package com.book.backend.domain.openapi.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LibSrchRequestDto implements OpenAPIRequestInterface {
    private String libCode;
    private String region;
    private String dtl_region;
    private String pageNo;
    private String pageSize;
}
