package com.book.backend.domain.opentalk.dto;

import com.book.backend.domain.message.dto.MessageResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class OpentalkJoinResponseDto {
    private final Long opentalkId;
    private final boolean isFavorite; //즐찾 여부
    private final List<MessageResponseDto> messageResponseDto; //nullable

    public static final String description = "opentalkId : 오픈톡 DB ID | isFavorite : 즐찾여부 | messageResponseDto : 0번째 페이지의 채팅 리스트";
}
