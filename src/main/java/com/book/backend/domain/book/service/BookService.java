package com.book.backend.domain.book.service;

import com.book.backend.domain.openapi.dto.request.HotTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.RecommendRequestDto;
import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.RecommendResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import java.util.HashSet;
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

        requestDto.setType("mania");
        JSONObject maniaJsonResponse = openAPI.connect(subUrl, requestDto, new RecommendResponseDto()); //반환값
        ResponseParser maniaResponseParser = new ResponseParser();
        LinkedList<RecommendResponseDto> maniaResponse = maniaResponseParser.recommend(maniaJsonResponse);

        requestDto.setType("reader");
        JSONObject readerJsonResponse = openAPI.connect(subUrl, requestDto, new RecommendResponseDto()); //반환값
        ResponseParser readerResponseParser = new ResponseParser();
        LinkedList<RecommendResponseDto> readerResponse = readerResponseParser.recommend(readerJsonResponse);

        LinkedList<RecommendResponseDto> responseList = new LinkedList<>();

        HashSet<String> duplicateCheckSet = new HashSet<>();
        for (RecommendResponseDto dto : maniaResponse) {
            String key = dto.getBookname() + dto.getAuthors();
            if (duplicateCheckSet.add(key)) responseList.add(dto);
        }
        for (RecommendResponseDto dto : readerResponse) {
            String key = dto.getBookname() + dto.getAuthors();
            if (duplicateCheckSet.add(key)) responseList.add(dto);
        }
        return responseList;
    }

    public LinkedList<HotTrendResponseDto> hotTrend(HotTrendRequestDto requestDto) throws Exception{
        String subUrl = "hotTrend";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new HotTrendResponseDto()); //반환값
        ResponseParser responseParser = new ResponseParser();
        return responseParser.hotTrend(jsonResponse);
    }
}
