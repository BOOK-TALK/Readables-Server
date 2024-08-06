package com.book.backend.domain.openapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomHotTrendRequestDto implements OpenAPIRequestInterface {
    private String startDt; // yyy-mm-dd 조회 시작일
    private String endDt;
    private String gender;
    private String from_age; // 시작연령
    private String to_age; // 종료연령
    private String age; // 나이대
    private String region;
    private String dtl_region; // 세부지역
    private String book_dvsn; // big:큰글씨도서, oversea:국외도서
    private String addCode; //isbn 부가기호?
    private String kdc; //대주제
    private String dtl_kdc; //세부주제
    private String pageNo; //응답결과 페이지 번호
    private String pageSize; //한 페이지 당 제공되는 도서 개수
}
