package com.book.backend.domain.goal.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalDto {
    private Long goalId;
    private String isbn;
    private String recentPage;
    private String totalPage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isFinished;
    private List<AWeekRecordsDto> recordsDtos;
}
