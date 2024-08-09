package com.book.backend.domain.openapi.dto.request;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MonthlyKeywordsRequestDto implements OpenAPIRequestInterface {
    private String searchDt;

    public void setSearchDt(){
        StringBuilder sb = new StringBuilder();
        int year = LocalDate.now().getYear();
        int lastMonth = LocalDate.now().getMonth().getValue() - 1;

        // 만약 month 가 한자리라면 앞에 0 붙이기
        if(lastMonth < 10) sb.append(year + "-0" + lastMonth);
        else sb.append(year + "-" + lastMonth);

        this.searchDt = sb.toString();
    }
}
