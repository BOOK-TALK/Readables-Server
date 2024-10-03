package com.book.backend.domain.goal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyProgressDto {
    private Long goalId;
    private Boolean isInProgress;
    private Double progressRate;

    public static final String description =
            "goalId: 목표 ID (목표가 없으면 null) | " +
            "isInProgress : 진행 중 여부 (완료되었거나 목표가 없으면 false) | " +
            "progressRate : 진행도 (완독율)";
}
