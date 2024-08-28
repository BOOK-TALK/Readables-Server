package com.book.backend.domain.goal.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordIntervalDto {
    private LocalDate date;
    private Integer pageInterval;

    public static final String description =
            "date : 날짜 | " +
            "pageInterval : 읽은 페이지 수 (이전에 기록된 날짜 대비)";
}
