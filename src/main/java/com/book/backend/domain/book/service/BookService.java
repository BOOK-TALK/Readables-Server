package com.book.backend.domain.book.service;

import com.book.backend.domain.openapi.dto.request.HotTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.RecommendRequestDto;
import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.RecommendResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    @Autowired
    private final OpenAPI openAPI;

    public LinkedList<RecommendResponseDto> recommend(RecommendRequestDto requestDto) throws Exception {
        String subUrl = "recommandList";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new RecommendResponseDto()); //반환값
        ResponseParser responseParser = new ResponseParser();
        return responseParser.recommend(jsonResponse);
    }

    public LinkedList<HotTrendResponseDto> hotTrend(HotTrendRequestDto requestDto) throws Exception{
        String subUrl = "hotTrend";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new HotTrendResponseDto()); //반환값
        ResponseParser responseParser = new ResponseParser();
        return responseParser.hotTrend(jsonResponse);
    }
}
