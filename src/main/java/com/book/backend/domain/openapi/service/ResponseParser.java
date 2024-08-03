package com.book.backend.domain.openapi.service;

import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.RecommendResponseDto;
import java.util.HashSet;
import java.util.LinkedList;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class ResponseParser {
    // TODO : API 별로 파싱 메소드 추가
    /*
    * hashSet에 isbn을 담고, 만약 이미 있는 isbn 이라면 list 에 추가 x
    * 없는 값이라면 list에 추가
    * if(set.add(isbn13)) list에 추가
    * */
    private boolean duplicateChecker(String duplicateCheckKey, HashSet<String> set){
        return set.add(duplicateCheckKey);
    }

    public LinkedList<RecommendResponseDto> recommend(JSONObject jsonResponse) {
        JSONArray step0 = (JSONArray) jsonResponse.get("docs");

        LinkedList<RecommendResponseDto> responseList = new LinkedList<>();
        HashSet<String> duplicateCheckSet = new HashSet<>();
        for (int i = 0; i < step0.size(); i++) {
            JSONObject temp = (JSONObject) step0.get(i);
            JSONObject step1 = (JSONObject) temp.get("book");

            // 중복 추천 체크 (open api 가 중복되는 책을 추천함;;)
            String duplicateCheckKey = step1.getAsString("bookname") + step1.getAsString("authors");
            if(duplicateChecker(duplicateCheckKey, duplicateCheckSet)){
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
        // TODO : 오늘 날짜 3일 이전부터 조회 제한
        JSONArray step0 = (JSONArray) jsonResponse.get("results");
        LinkedList<HotTrendResponseDto> responseList = new LinkedList<>();

        for(int i = 0; i < step0.size(); i++) {
            JSONObject temp1 = (JSONObject) step0.get(0);
            JSONObject step1 = (JSONObject) temp1.get("result");
            JSONArray step2 = (JSONArray) step1.get("docs");
            for (int j = 0; j < step2.size(); j++) {
                JSONObject temp3 = (JSONObject) step2.get(j);
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
}
