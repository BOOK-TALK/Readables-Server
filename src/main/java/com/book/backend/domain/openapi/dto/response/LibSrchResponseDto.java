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

    public static final String description =
            "libCode : 도서관 코드 | " +
            "libName : 도서관명 | " +
            "address : 주소 | " +
            "tel : 전화번호 | " +
            "latitude : 위도 | " +
            "longitude : 경도 | " +
            "hompage : 홈페이지 URL | " +
            "closed : 휴관일 | " +
            "operatingTime : 운영시간 | " +
            "BookCount : 단행본수";
}
