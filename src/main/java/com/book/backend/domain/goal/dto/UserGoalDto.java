package com.book.backend.domain.goal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGoalDto {
    private String nickname;
    private String finishRate;
}
