package com.book.backend.domain.openapi.service;

import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.RecommendResponseDto;

import java.util.*;
import java.util.stream.IntStream;

import com.book.backend.domain.openapi.dto.response.LoanTrendResponseDto;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class ResponseParser {

    public LinkedList<RecommendResponseDto> recommend(JSONObject jsonResponse) {
        JSONArray docs = (JSONArray) jsonResponse.get("docs");

        LinkedList<RecommendResponseDto> responseList = new LinkedList<>();
        HashSet<String> duplicateCheckSet = new HashSet<>();
        for (Object o : docs) {
            JSONObject docsElement  = (JSONObject) o;
            JSONObject book = (JSONObject) docsElement.get("book");

            // 중복 추천 체크 (open api 가 중복되는 책을 추천함;;)
            String duplicateCheckKey = book.getAsString("bookname") + book.getAsString("authors");
            if (duplicateCheckSet.add(duplicateCheckKey)) { // 중복 확인
                responseList.add(RecommendResponseDto.builder()
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

    public LinkedList<LoanTrendResponseDto> periodTrend(JSONObject jsonResponse, Integer maxSize) {
        JSONArray docs = (JSONArray) jsonResponse.get("docs");
        LinkedList<LoanTrendResponseDto> responseList = new LinkedList<>();

        for (Object o : docs) {
            if (maxSize != null && responseList.size() >= maxSize) {
                break;
            }

            JSONObject docsElement = (JSONObject) o;
            JSONObject doc = (JSONObject) docsElement.get("doc");

            responseList.add(LoanTrendResponseDto.builder()
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
        return responseList;
    }

    public LinkedList<LoanTrendResponseDto> random(JSONObject jsonResponse, int resultSize, Integer maxSize) {
        JSONArray docs = (JSONArray) jsonResponse.get("docs");
        LinkedList<LoanTrendResponseDto> responseList = new LinkedList<>();

        List<Integer> idxs = new ArrayList<>(IntStream.range(0, docs.size()).boxed().toList());
        Collections.shuffle(idxs);
        List<Integer> resultIdxs = idxs.subList(0, resultSize);

        for (int i = 0; i < resultSize; i++) {
            if (maxSize != null && responseList.size() >= maxSize) {
                break;
            }

            int idx = resultIdxs.get(i);
            JSONObject docsElement = (JSONObject) docs.get(idx);
            JSONObject doc = (JSONObject) docsElement.get("doc");

            responseList.add(LoanTrendResponseDto.builder()
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
        return responseList;
    }

    public LinkedList<LoanTrendResponseDto> newTrend(JSONObject jsonResponse, int currentYear, Integer maxSize) {
        JSONArray docs = (JSONArray) jsonResponse.get("docs");
        LinkedList<LoanTrendResponseDto> responseList = new LinkedList<>();

        for (Object o : docs) {
            if (maxSize != null && responseList.size() >= maxSize) {
                break;
            }

            JSONObject docsElement = (JSONObject) o;
            JSONObject doc = (JSONObject) docsElement.get("doc");

            int publication_year;
            try {
                publication_year = Integer.parseInt(doc.getAsString("publication_year"));
            } catch (NumberFormatException e) {
                continue;
            }

            if (publication_year >= currentYear - 2) {
                responseList.add(LoanTrendResponseDto.builder()
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
}
