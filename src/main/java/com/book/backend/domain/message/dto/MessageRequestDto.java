package com.book.backend.domain.message.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class MessageRequestDto {
    private String type; //text, img
    private String jwtToken;
    private Long opentalkId;
    private String content;
}
