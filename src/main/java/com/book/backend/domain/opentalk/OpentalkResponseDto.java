package com.book.backend.domain.opentalk;

import com.book.backend.domain.message.entity.Message;
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
    private List<Long> hotOpentalkList;
    private List<Long> favoriteOpentalkList;

    public static final String description = "hotOpentalkList : 현재 핫한 오픈톡 id List | favoriteOpentalkList : 내가 즐겨찾기한 오픈톡 id List";
}
