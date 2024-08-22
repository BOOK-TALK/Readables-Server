package com.book.backend.domain.opentalk.dto;

import com.book.backend.domain.message.dto.MessageResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class OpentalkJoinResponseDto {
    private final Long opentalkId;
    private final List<MessageResponseDto> messageResponseDto; //nullable
}
