package com.book.backend.domain.message.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequestDto {
//    private String type; //text, img
    private String content;
}
