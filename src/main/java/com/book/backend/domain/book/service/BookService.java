package com.book.backend.domain.book.service;

import com.book.backend.domain.openapi.dto.request.HotTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.KeywordRequestDto;
import com.book.backend.domain.openapi.dto.request.RecommendRequestDto;
import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.KeywordResponseDto;
import com.book.backend.domain.openapi.dto.response.RecommendResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import java.util.HashSet;
import java.util.Iterator;
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

        LinkedList<RecommendResponseDto> responseList = new LinkedList<>();

        requestDto.setType("mania");
        JSONObject maniaJsonResponse = openAPI.connect(subUrl, requestDto, new RecommendResponseDto());
        ResponseParser maniaResponseParser = new ResponseParser();
        responseList.addAll(maniaResponseParser.recommend(maniaJsonResponse));

        requestDto.setType("reader");
        JSONObject readerJsonResponse = openAPI.connect(subUrl, requestDto, new RecommendResponseDto());
        ResponseParser readerResponseParser = new ResponseParser();
        responseList.addAll(readerResponseParser.recommend(readerJsonResponse));

        return responseList;
    }

    /* 추천 책 수가 5보다 작으면 추천된 isbn 으로 recommend() 다시 호출 */
    public void ensureRecommendationsCount(LinkedList<RecommendResponseDto> list, HashSet<String> set) throws Exception {
        LinkedList<RecommendResponseDto> originalList = new LinkedList<>(list);
        Iterator<RecommendResponseDto> iterator = originalList.iterator();
        while (originalList.size() < 5 && iterator.hasNext()) {
            RecommendRequestDto newRequestDto =  RecommendRequestDto.builder().isbn13(iterator.next().getIsbn13()).build(); // 추천된 다른 책의 isbn13
            LinkedList<RecommendResponseDto> newRecommendList = recommend(newRequestDto);
            // 기존에 추가된 책인지 확인
            for(RecommendResponseDto dto : newRecommendList){
                String key = dto.getBookname() + dto.getAuthors();
                if(set.add(key)) list.add(dto);
            }
        }
    }

    public LinkedList<RecommendResponseDto> duplicateChecker(LinkedList<RecommendResponseDto> list, HashSet<String> set){
        LinkedList<RecommendResponseDto> duplicateRemovedList = new LinkedList<>();
        for(RecommendResponseDto dto : list){
            String key = dto.getBookname() + dto.getAuthors();
            if(set.add(key)) duplicateRemovedList.add(dto);
        }
        return duplicateRemovedList;
    }

    public LinkedList<HotTrendResponseDto> hotTrend(HotTrendRequestDto requestDto) throws Exception{
        String subUrl = "hotTrend";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new HotTrendResponseDto());
        ResponseParser responseParser = new ResponseParser();
        return responseParser.hotTrend(jsonResponse);
    }

    public LinkedList<KeywordResponseDto> keywords(KeywordRequestDto requestDto) throws Exception{
        String subUrl = "monthlyKeywords";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new KeywordResponseDto());
        ResponseParser responseParser = new ResponseParser();
        return responseParser.keywords(jsonResponse);
    }
}
