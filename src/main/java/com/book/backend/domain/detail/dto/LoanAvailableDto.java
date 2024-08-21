package com.book.backend.domain.detail.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoanAvailableDto {
    private String libCode;
    private boolean isLoanable;
}
