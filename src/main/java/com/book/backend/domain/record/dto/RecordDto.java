package com.book.backend.domain.record.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RecordDto {
    private LocalDate date;
    private Integer recentPage;
}
