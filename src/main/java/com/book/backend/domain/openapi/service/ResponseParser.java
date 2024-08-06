package com.book.backend.domain.openapi.service;

import com.book.backend.domain.openapi.dto.response.CustomHotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.KeywordResponseDto;
import com.book.backend.domain.openapi.dto.response.RecommendResponseDto;
import java.util.HashSet;
import java.util.LinkedList;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Slf4j
public class ResponseParser {

    public LinkedList<RecommendResponseDto> recommend(JSONObject jsonResponse) {
        log.trace("ResponseParser > recommend()");

        JSONArray step0 = (JSONArray) jsonResponse.get("docs");
        LinkedList<RecommendResponseDto> responseList = new LinkedList<>();
        HashSet<String> duplicateCheckSet = new HashSet<>();
        for (Object obj : step0) {
            JSONObject temp = (JSONObject) obj;
            JSONObject step1 = (JSONObject) temp.get("book");

            // 중복 추천 체크 (open api 가 중복되는 책을 추천함;;)
            String duplicateCheckKey = step1.getAsString("bookname") + step1.getAsString("authors");
            if (duplicateCheckSet.add(duplicateCheckKey)) { // 중복 확인
                responseList.add(RecommendResponseDto.builder()
                        .bookname(step1.getAsString("bookname"))
                        .authors(step1.getAsString("authors"))
                        .publisher(step1.getAsString("publisher"))
                        .publication_year(step1.getAsString("publication_year"))
                        .isbn13(step1.getAsString("isbn13"))
                        .additional_symbol(step1.getAsString("additional_symbol"))
                        .vol(step1.getAsString("vol"))
                        .class_no(step1.getAsString("class_no"))
                        .class_nm(step1.getAsString("class_nm"))
                        .bookImageURL(step1.getAsString("bookImageURL"))
                        .build());
            }
        }
        return responseList;
    }

    public LinkedList<HotTrendResponseDto> hotTrend(JSONObject jsonResponse) {
        log.trace("ResponseParser > hotTrend()");

        JSONArray step0 = (JSONArray) jsonResponse.get("results");
        LinkedList<HotTrendResponseDto> responseList = new LinkedList<>();

        for (int i = 0; i < step0.size(); i++) {
            JSONObject temp1 = (JSONObject) step0.get(i);
            JSONObject step1 = (JSONObject) temp1.get("result");
            JSONArray step2 = (JSONArray) step1.get("docs");
            for (Object obj : step2) {
                JSONObject temp3 = (JSONObject) obj;
                JSONObject step3 = (JSONObject) temp3.get("doc");

                responseList.add(HotTrendResponseDto.builder()
                        .no(step3.getAsString("no"))
                        .difference(step3.getAsString("difference"))
                        .baseWeekRank(step3.getAsString("baseWeekRank"))
                        .pastWeekRank(step3.getAsString("pastWeekRank"))
                        .bookname(step3.getAsString("bookname"))
                        .authors(step3.getAsString("authors"))
                        .publisher(step3.getAsString("publisher"))
                        .publication_year(step3.getAsString("publication_year"))
                        .isbn13(step3.getAsString("isbn13"))
                        .additional_symbol(step3.getAsString("additional_symbol"))
                        .vol(step3.getAsString("vol"))
                        .class_no(step3.getAsString("class_no"))
                        .class_nm(step3.getAsString("class_nm"))
                        .bookImageURL(step3.getAsString("bookImageURL"))
                        .bookDtlUrl(step3.getAsString("bookDtlUrl"))
                        .build());
            }
        }
        return responseList;
    }

    public LinkedList<KeywordResponseDto> keywords(JSONObject jsonResponse) {
        log.trace("ResponseParser > keywords()");

        JSONArray step0 = (JSONArray) jsonResponse.get("keywords");
        LinkedList<KeywordResponseDto> responseList = new LinkedList<>();

        for (Object obj : step0) {
            JSONObject temp = (JSONObject) obj;
            JSONObject step1 = (JSONObject) temp.get("keyword");

            responseList.add(KeywordResponseDto.builder()
                    .keyword(step1.getAsString("word"))
                    .weight(step1.getAsString("weight"))
                    .build());
        }
        return RandomPicker.randomPick(responseList, 10, true);
    }

    public LinkedList<CustomHotTrendResponseDto> customHotTrend(JSONObject jsonResponse) {
        log.trace("ResponseParser > customHotTrend()");

        JSONArray step0 = (JSONArray) jsonResponse.get("docs");

        LinkedList<CustomHotTrendResponseDto> responseList = new LinkedList<>();
        HashSet<String> duplicateCheckSet = new HashSet<>();
        for (Object obj : step0) {
            JSONObject temp = (JSONObject) obj;
            JSONObject step1 = (JSONObject) temp.get("doc");

            responseList.add(CustomHotTrendResponseDto.builder()
                    .no(step1.getAsString("no"))
                    .ranking(step1.getAsString("ranking"))
                    .bookname(step1.getAsString("bookname"))
                    .authors(step1.getAsString("authors"))
                    .publisher(step1.getAsString("publisher"))
                    .publication_year(step1.getAsString("publication_year"))
                    .isbn13(step1.getAsString("isbn13"))
                    .additional_symbol(step1.getAsString("additional_symbol"))
                    .vol(step1.getAsString("vol"))
                    .class_no(step1.getAsString("class_no"))
                    .class_nm(step1.getAsString("class_nm"))
                    .loan_count(step1.getAsString("loan_count"))
                    .bookImageURL(step1.getAsString("bookImageURL"))
                    .bookDtlUrl(step1.getAsString("bookDtlUrl"))
                    .build());
        }
        return responseList;
    }
}
