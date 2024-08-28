package com.book.backend.domain.goal.dto;

import com.book.backend.domain.book.dto.BookSummaryDto;
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
    private String userNickname;
    private BookSummaryDto bookSummary;
    private Integer recentPage;
    private Integer totalPage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isFinished;
    private List<RecordDto> aWeekRecords;

    public static final String description =
            "goalId : 목표 아이디 | " +
            "userNickname : 목표 진행 중인 유저 닉네임 | " +
            "bookSummary : 도서 요약 정보 | " +
            "recentPage : 중단한 페이지 | " +
            "totalPage : 총 페이지 | " +
            "createdAt : 생성 시각 | " +
            "updatedAt : 최근 업데이트된 시각 | " +
            "isFinished : 완료 여부 | " +
            "records : 일주일 간 기록";
}
