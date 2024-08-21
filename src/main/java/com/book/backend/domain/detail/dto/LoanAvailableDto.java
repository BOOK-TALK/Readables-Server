package com.book.backend.domain.detail.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoanAvailableDto {
    private String libCode;
    private String libName;
    private boolean isLoanable;
}
