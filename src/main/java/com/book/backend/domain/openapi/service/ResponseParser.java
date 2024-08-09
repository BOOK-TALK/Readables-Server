package com.book.backend.domain.openapi.service;

import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.MonthlyKeywordsResponseDto;
import com.book.backend.domain.openapi.dto.response.RecommendListResponseDto;

import com.book.backend.domain.openapi.dto.response.LoanItemSrchResponseDto;

import java.util.HashSet;
import java.util.LinkedList;
import lombok.extern.slf4j.Slf4j;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResponseParser {

    public LinkedList<RecommendListResponseDto> recommend(JSONObject jsonResponse) {
        log.trace("ResponseParser > recommend()");


        JSONArray docs = (JSONArray) jsonResponse.get("docs");
        LinkedList<RecommendListResponseDto> responseList = new LinkedList<>();
        HashSet<String> duplicateCheckSet = new HashSet<>();

        for (Object o : docs) {
            JSONObject docsElement  = (JSONObject) o;
            JSONObject book = (JSONObject) docsElement.get("book");

            // 중복 추천 체크 (open api 가 중복되는 책을 추천함;;)
            String duplicateCheckKey = book.getAsString("bookname") + book.getAsString("authors");

            if (duplicateCheckSet.add(duplicateCheckKey)) { // 중복 확인
                responseList.add(RecommendListResponseDto.builder()
                        .bookname(book.getAsString("bookname"))
                        .authors(book.getAsString("authors"))
                        .publisher(book.getAsString("publisher"))
                        .publication_year(book.getAsString("publication_year"))
                        .isbn13(book.getAsString("isbn13"))
                        .vol(book.getAsString("vol"))
                        .class_no(book.getAsString("class_no"))
                        .class_nm(book.getAsString("class_nm"))
                        .bookImageURL(book.getAsString("bookImageURL"))
                        .build());
            }
        }
        return responseList;
    }

    public LinkedList<HotTrendResponseDto> hotTrend(JSONObject jsonResponse) {
        log.trace("ResponseParser > hotTrend()");
        
        JSONArray results = (JSONArray) jsonResponse.get("results");
        LinkedList<HotTrendResponseDto> responseList = new LinkedList<>();

        for (Object o : results) {
            JSONObject resultsElement = (JSONObject) o;
            JSONObject result = (JSONObject) resultsElement.get("result");
            JSONArray docs = (JSONArray) result.get("docs");
            for (Object value : docs) {
                JSONObject docsElement = (JSONObject) value;
                JSONObject doc = (JSONObject) docsElement.get("doc");

                responseList.add(HotTrendResponseDto.builder()
                        .no(doc.getAsString("no"))
                        .difference(doc.getAsString("difference"))
                        .baseWeekRank(doc.getAsString("baseWeekRank"))
                        .pastWeekRank(doc.getAsString("pastWeekRank"))
                        .bookname(doc.getAsString("bookname"))
                        .authors(doc.getAsString("authors"))
                        .publisher(doc.getAsString("publisher"))
                        .publication_year(doc.getAsString("publication_year"))
                        .isbn13(doc.getAsString("isbn13"))
                        .addition_symbol(doc.getAsString("addition_symbol"))
                        .vol(doc.getAsString("vol"))
                        .class_no(doc.getAsString("class_no"))
                        .class_nm(doc.getAsString("class_nm"))
                        .bookImageURL(doc.getAsString("bookImageURL"))
                        .bookDtlUrl(doc.getAsString("bookDtlUrl"))
                        .build());
            }
        }
        return responseList;
    }

    public LinkedList<LoanItemSrchResponseDto> loanTrend(JSONObject jsonResponse) {
        JSONArray docs = (JSONArray) jsonResponse.get("docs");

        LinkedList<LoanItemSrchResponseDto> responseList = new LinkedList<>();
        HashSet<String> duplicateCheckSet = new HashSet<>();

        for (Object o : docs) {

            JSONObject docsElement = (JSONObject) o;
            JSONObject doc = (JSONObject) docsElement.get("doc");

            String duplicateCheckKey = doc.getAsString("bookname") + doc.getAsString("authors");
            if (duplicateCheckSet.add(duplicateCheckKey)) {
                responseList.add(LoanItemSrchResponseDto.builder()
                        .no(doc.getAsString("no"))
                        .ranking(doc.getAsString("ranking"))
                        .bookname(doc.getAsString("bookname"))
                        .authors(doc.getAsString("authors"))
                        .publisher(doc.getAsString("publisher"))
                        .publication_year(doc.getAsString("publication_year"))
                        .isbn13(doc.getAsString("isbn13"))
                        .addition_symbol(doc.getAsString("addition_symbol"))
                        .class_no(doc.getAsString("class_no"))
                        .class_nm(doc.getAsString("class_nm"))
                        .loan_count(doc.getAsString("loan_count"))
                        .bookImageURL(doc.getAsString("bookImageURL"))
                        .bookDtlUrl(doc.getAsString("bookDtlUrl"))
                        .build());
            }
        }
        return responseList;
    }

    public LinkedList<MonthlyKeywordsResponseDto> keywords(JSONObject jsonResponse) {
        log.trace("ResponseParser > keywords()");

        JSONArray step0 = (JSONArray) jsonResponse.get("keywords");
        LinkedList<MonthlyKeywordsResponseDto> responseList = new LinkedList<>();

        for (Object obj : step0) {
            JSONObject temp = (JSONObject) obj;
            JSONObject step1 = (JSONObject) temp.get("keyword");

            responseList.add(MonthlyKeywordsResponseDto.builder()
                    .keyword(step1.getAsString("word"))
                    .weight(step1.getAsString("weight"))
                    .build());
        }
        return RandomPicker.randomPick(responseList, 10);
    }

    public LinkedList<LoanItemSrchResponseDto> loanItemSrch(JSONObject jsonResponse) {
        log.trace("ResponseParser > loanItemSrch()");

        JSONArray step0 = (JSONArray) jsonResponse.get("docs");

        LinkedList<LoanItemSrchResponseDto> responseList = new LinkedList<>();
        HashSet<String> duplicateCheckSet = new HashSet<>();
        for (Object obj : step0) {
            JSONObject temp = (JSONObject) obj;
            JSONObject step1 = (JSONObject) temp.get("doc");

            responseList.add(LoanItemSrchResponseDto.builder()
                    .no(step1.getAsString("no"))
                    .ranking(step1.getAsString("ranking"))
                    .bookname(step1.getAsString("bookname"))
                    .authors(step1.getAsString("authors"))
                    .publisher(step1.getAsString("publisher"))
                    .publication_year(step1.getAsString("publication_year"))
                    .isbn13(step1.getAsString("isbn13"))
                    .addition_symbol(step1.getAsString("addition_symbol"))
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
