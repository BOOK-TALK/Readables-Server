package com.book.backend.domain.opentalk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotOpentalkResponseDto {
    private List<OpentalkDto> hotOpentalkList;

    public static final String description = "hotOpentalkList : 현재 핫한 오픈톡 List";
}
