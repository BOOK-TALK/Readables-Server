package com.book.backend.domain.goal.dto;

import com.book.backend.domain.book.dto.BookSummaryDto;
import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.domain.user.dto.UserSummaryDto;
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
    private UserSummaryDto userSummary;
    private BookSummaryDto bookSummary;
    private String recentPage;
    private String totalPage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isFinished;
    private List<AWeekRecordsDto> recordsDtos;
}
