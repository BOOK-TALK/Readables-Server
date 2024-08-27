package com.book.backend.domain.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDto {
    private Long userId;
    private String nickname;
}
