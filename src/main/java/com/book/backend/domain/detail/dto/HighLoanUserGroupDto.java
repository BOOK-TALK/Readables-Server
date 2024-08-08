package com.book.backend.domain.detail.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HighLoanUserGroupDto {
    private String age;
    private String gender;
    private String loanCnt;
    private String ranking;
}
