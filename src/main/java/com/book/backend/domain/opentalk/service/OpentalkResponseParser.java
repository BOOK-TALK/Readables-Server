package com.book.backend.domain.opentalk.service;

import com.book.backend.domain.openapi.dto.response.DetailResponseDto;
import com.book.backend.domain.opentalk.dto.OpentalkDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpentalkResponseParser {
    public void setSimpleBookInfo(OpentalkDto opentalkDto, DetailResponseDto responseDto){
        log.trace("OpentalkResponseParser > setSimpleBookInfo()");

        opentalkDto.setBookName(responseDto.getBookInfoDto().getBookname());
        opentalkDto.setBookImageURL(responseDto.getBookInfoDto().getBookImageURL());
    }
}
