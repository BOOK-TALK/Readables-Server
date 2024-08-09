package com.book.backend.domain.book.service;

import com.book.backend.domain.openapi.dto.request.HotTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.LoanItemSrchRequestDto;
import com.book.backend.domain.openapi.dto.request.MonthlyKeywordsRequestDto;
import com.book.backend.domain.openapi.dto.request.RecommendListRequestDto;
import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.LoanItemSrchResponseDto;
import com.book.backend.domain.openapi.dto.response.MonthlyKeywordsResponseDto;
import com.book.backend.domain.openapi.dto.response.RecommendListResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;

import java.util.HashSet;
import java.util.Iterator;
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
public class BookService {
    private final OpenAPI openAPI;

    public LinkedList<RecommendListResponseDto> recommend(RecommendListRequestDto requestDto) throws Exception {
        log.trace("recommend()");
        String subUrl = "recommandList";

        LinkedList<RecommendListResponseDto> responseList = new LinkedList<>();

        requestDto.setType("mania");
        JSONObject maniaJsonResponse = openAPI.connect(subUrl, requestDto, new RecommendListResponseDto());
        ResponseParser maniaResponseParser = new ResponseParser();
        responseList.addAll(maniaResponseParser.recommend(maniaJsonResponse));

        requestDto.setType("reader");
        JSONObject readerJsonResponse = openAPI.connect(subUrl, requestDto, new RecommendListResponseDto());
        ResponseParser readerResponseParser = new ResponseParser();
        responseList.addAll(readerResponseParser.recommend(readerJsonResponse));

        return responseList;
    }

    /* 추천 책 수가 5보다 작으면 추천된 isbn 으로 recommend() 다시 호출 */
    public void ensureRecommendationsCount(LinkedList<RecommendListResponseDto> list, HashSet<String> set) throws Exception {
        log.trace("ensureRecommendationsCount()");
        LinkedList<RecommendListResponseDto> originalList = new LinkedList<>(list);
        Iterator<RecommendListResponseDto> iterator = originalList.iterator();
        while (originalList.size() < 5 && iterator.hasNext()) {
            RecommendListRequestDto newRequestDto =  RecommendListRequestDto.builder().isbn13(iterator.next().getIsbn13()).build(); // 추천된 다른 책의 isbn13
            LinkedList<RecommendListResponseDto> newRecommendList = recommend(newRequestDto);
            // 기존에 추가된 책인지 확인
            for(RecommendListResponseDto dto : newRecommendList){
                String key = dto.getBookname() + dto.getAuthors();
                if(set.add(key)) list.add(dto);
            }
        }
    }

    public LinkedList<RecommendListResponseDto> duplicateChecker(LinkedList<RecommendListResponseDto> list, HashSet<String> set){
        log.trace("duplicateChecker()");
        LinkedList<RecommendListResponseDto> duplicateRemovedList = new LinkedList<>();
        for(RecommendListResponseDto dto : list){
            String key = dto.getBookname() + dto.getAuthors();
            if(set.add(key)) duplicateRemovedList.add(dto);
        }
        return duplicateRemovedList;
    }

    public LinkedList<HotTrendResponseDto> hotTrend(HotTrendRequestDto requestDto) throws Exception{
        log.trace("hotTrend()");
        String subUrl = "hotTrend";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new HotTrendResponseDto());
        ResponseParser responseParser = new ResponseParser();
        return responseParser.hotTrend(jsonResponse);
    }

    public LinkedList<MonthlyKeywordsResponseDto> keywords(MonthlyKeywordsRequestDto requestDto) throws Exception{
        log.trace("keywords()");
        String subUrl = "monthlyKeywords";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new MonthlyKeywordsResponseDto());
        ResponseParser responseParser = new ResponseParser();
        return responseParser.keywords(jsonResponse);
    }

    public LinkedList<LoanItemSrchResponseDto> loanItemSrch(LoanItemSrchRequestDto requestDto) throws Exception{
        log.trace("customHotTrend()");
        String subUrl = "loanItemSrch";
        requestDto.setPageSize("10"); // 10개만 출력하도록 제한
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new LoanItemSrchResponseDto());
        ResponseParser responseParser = new ResponseParser();
        return responseParser.loanItemSrch(jsonResponse);
    }
}
