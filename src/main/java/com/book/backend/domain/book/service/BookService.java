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
        log.trace("BookService > recommend()");
        String subUrl = "recommandList";

        LinkedList<RecommendListResponseDto> responseList = new LinkedList<>();

        requestDto.setType("mania");
        JSONObject maniaJsonResponse = openAPI.connect(subUrl, requestDto, new RecommendListResponseDto(), 1);
        ResponseParser maniaResponseParser = new ResponseParser();
        responseList.addAll(maniaResponseParser.recommend(maniaJsonResponse));

        requestDto.setType("reader");
        JSONObject readerJsonResponse = openAPI.connect(subUrl, requestDto, new RecommendListResponseDto(), 1);
        ResponseParser readerResponseParser = new ResponseParser();
        responseList.addAll(readerResponseParser.recommend(readerJsonResponse));

        return responseList;
    }

    public LinkedList<RecommendListResponseDto> duplicateChecker(LinkedList<RecommendListResponseDto> list, HashSet<String> set){
        log.trace("BookService > duplicateChecker()");
        LinkedList<RecommendListResponseDto> duplicateRemovedList = new LinkedList<>();
        for(RecommendListResponseDto dto : list){
            String key = dto.getBookname() + dto.getAuthors();
            if(set.add(key)) duplicateRemovedList.add(dto);
        }
        return duplicateRemovedList;
    }

    public LinkedList<HotTrendResponseDto> hotTrend(HotTrendRequestDto requestDto) throws Exception{
        log.trace("BookService > hotTrend()");
        String subUrl = "hotTrend";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new HotTrendResponseDto(), 1);
        ResponseParser responseParser = new ResponseParser();
        return responseParser.hotTrend(jsonResponse);
    }

    public LinkedList<MonthlyKeywordsResponseDto> keywords(MonthlyKeywordsRequestDto requestDto) throws Exception{
        log.trace("BookService > keywords()");
        String subUrl = "monthlyKeywords";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new MonthlyKeywordsResponseDto(), 1);
        ResponseParser responseParser = new ResponseParser();
        return responseParser.keywords(jsonResponse);
    }

    public LinkedList<LoanItemSrchResponseDto> loanItemSrch(LoanItemSrchRequestDto requestDto) throws Exception{
        log.trace("BookService > customHotTrend()");
        String subUrl = "loanItemSrch";
        requestDto.setPageSize("25");
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new LoanItemSrchResponseDto(), 1);
        ResponseParser responseParser = new ResponseParser();
        LinkedList<LoanItemSrchResponseDto> responseList = responseParser.loanItemSrch(jsonResponse);
        return responseParser.customPageFilter(responseList, "1", "10");
    }
}
