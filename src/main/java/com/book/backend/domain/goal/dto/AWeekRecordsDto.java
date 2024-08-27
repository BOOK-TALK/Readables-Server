package com.book.backend.domain.goal.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AWeekRecordsDto {
    private LocalDate date;
    private Integer pageCount;
}
