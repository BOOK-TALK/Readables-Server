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
public class OpentalkResponseDto {
    // 책 제목이랑 이미지 같이 보내기
    private List<OpentalkDto> hotOpentalkList;
    private List<OpentalkDto> favoriteOpentalkList;

    public static final String description = "hotOpentalkList : 현재 핫한 오픈톡 List | favoriteOpentalkList : 내가 즐겨찾기한 오픈톡 List";
}
