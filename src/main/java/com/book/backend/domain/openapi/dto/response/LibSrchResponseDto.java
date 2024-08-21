package com.book.backend.domain.openapi.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibSrchResponseDto implements OpenAPIResponseInterface {
    private String libCode;
    private String libName;
    private String address;
    private String tel;
    private String fax;
    private String latitude;
    private String longitude;
    private String homepage;
    private String closed;
    private String operatingTime;
    private String BookCount;
}
