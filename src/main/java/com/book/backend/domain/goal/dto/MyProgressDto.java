package com.book.backend.domain.goal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyProgressDto {
    private Boolean isInProgress;
    private Double progressRate;

    public static final String description =
            "isInProgress : 진행 중 여부 (완료되었거나 목표가 없다면 false) | " +
            "progressRate : 진행도 (완독율)";
}
