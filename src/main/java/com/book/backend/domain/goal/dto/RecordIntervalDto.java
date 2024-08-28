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
}
