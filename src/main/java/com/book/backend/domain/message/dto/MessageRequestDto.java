package com.book.backend.domain.message.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageRequestDto {
    private Long opentalkId;
    private String loginId;
    private String content;
}
