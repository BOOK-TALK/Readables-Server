package com.book.backend.domain.search.service;

import com.book.backend.domain.openapi.dto.request.SearchRequestDto;
import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.SearchResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SearchService {
    private final OpenAPI openAPI;

    public LinkedList<SearchResponseDto> search(SearchRequestDto requestDto) throws Exception {
        log.trace("search()");
        String subUrl = "srchBooks";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new HotTrendResponseDto());
        ResponseParser responseParser = new ResponseParser();
        return responseParser.search(jsonResponse);
    }
}
