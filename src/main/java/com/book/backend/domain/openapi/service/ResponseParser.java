package com.book.backend.domain.openapi.service;

import com.book.backend.domain.book.dto.BookInfoDto;
import com.book.backend.domain.detail.dto.CoLoanBooksDto;
import com.book.backend.domain.detail.dto.Top3LoanUserDto;
import com.book.backend.domain.detail.dto.RecommendDto;
import com.book.backend.domain.openapi.dto.response.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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

        if (docs == null) {
            return responseList;
        }

        for (Object o : docs) {
            JSONObject docsElement  = (JSONObject) o;
            JSONObject book = (JSONObject) docsElement.get("book");

            // 중복 체크
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
        HashSet<String> duplicateCheckSet = new HashSet<>();

        if (results == null) {
            return responseList;
        }

        for (Object o : results) {
            JSONObject resultsElement = (JSONObject) o;
            JSONObject result = (JSONObject) resultsElement.get("result");
            JSONArray docs = (JSONArray) result.get("docs");
            for (Object value : docs) {
                JSONObject docsElement = (JSONObject) value;
                JSONObject doc = (JSONObject) docsElement.get("doc");

                // 중복 체크
                String duplicateCheckKey = doc.getAsString("bookname") + doc.getAsString("authors");

                if (duplicateCheckSet.add(duplicateCheckKey)) { // 중복 확인
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
        }
        return responseList;
    }

    public LinkedList<LoanItemSrchResponseDto> loanTrend(JSONObject jsonResponse) {
        JSONArray docs = (JSONArray) jsonResponse.get("docs");

        LinkedList<LoanItemSrchResponseDto> responseList = new LinkedList<>();
        HashSet<String> duplicateCheckSet = new HashSet<>();

        if (docs == null) {
            return responseList;
        }

        for (Object o : docs) {

            JSONObject docsElement = (JSONObject) o;
            JSONObject doc = (JSONObject) docsElement.get("doc");

            // 중복 체크
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

        JSONArray keywords = (JSONArray) jsonResponse.get("keywords");
        LinkedList<MonthlyKeywordsResponseDto> responseList = new LinkedList<>();

        if (keywords == null) {
            return responseList;
        }

        for (Object o : keywords) {
            JSONObject keywordsElement = (JSONObject) o;
            JSONObject keyword = (JSONObject) keywordsElement.get("keyword");

            responseList.add(MonthlyKeywordsResponseDto.builder()
                    .keyword(keyword.getAsString("word"))
                    .weight(keyword.getAsString("weight"))
                    .build());
        }
        return RandomPicker.randomPick(responseList, 10);
    }

    public LinkedList<LoanItemSrchResponseDto> loanItemSrch(JSONObject jsonResponse) {
        log.trace("ResponseParser > loanItemSrch()");

        JSONArray docs = (JSONArray) jsonResponse.get("docs");

        LinkedList<LoanItemSrchResponseDto> responseList = new LinkedList<>();
        HashSet<String> duplicateCheckSet = new HashSet<>();

        if (docs == null) {
            return responseList;
        }

        for (Object o : docs) {
            JSONObject docsElement = (JSONObject) o;
            JSONObject doc = (JSONObject) docsElement.get("doc");

            // 중복 체크
            String duplicateCheckKey = doc.getAsString("bookname") + doc.getAsString("authors");

            if (duplicateCheckSet.add(duplicateCheckKey)) { // 중복 확인
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

    public LinkedList<SearchResponseDto> search(JSONObject jsonResponse){
        log.trace("ResponseParser > search()");

        JSONArray docs = (JSONArray) jsonResponse.get("docs");
        LinkedList<SearchResponseDto> responseList = new LinkedList<>();
        HashSet<String> duplicateCheckSet = new HashSet<>();

        if (docs == null) {
            return responseList;
        }

        for (Object o : docs) {
            JSONObject docsElement = (JSONObject) o;
            JSONObject doc = (JSONObject) docsElement.get("doc");

            // 중복 체크
            String duplicateCheckKey = doc.getAsString("bookname") + doc.getAsString("authors");

            if (duplicateCheckSet.add(duplicateCheckKey)) { // 중복 확인
                responseList.add(SearchResponseDto.builder()
                        .bookname(doc.getAsString("bookname"))
                        .authors(doc.getAsString("authors"))
                        .publisher(doc.getAsString("publisher"))
                        .publication_year(doc.getAsString("publication_year"))
                        .isbn13(doc.getAsString("isbn13"))
                        .vol(doc.getAsString("vol"))
                        .bookImageURL(doc.getAsString("bookImageURL"))
                        .bookDtlUrl(doc.getAsString("bookDtlUrl"))
                        .loan_count(doc.getAsString("loan_count"))
                        .build());
            }
        }
        return responseList;
    }

    public boolean loanAvailable(JSONObject jsonResponse) {
        log.trace("ResponseParser > loanAvailable()");

        JSONObject result = (JSONObject) jsonResponse.get("result");
        return result.getAsString("loanAvailable").equals("Y");
    }

    public DetailResponseDto detail(JSONObject jsonResponse){
        log.trace("ResponseParser > detail()");
        BookInfoDto bookInfoDto = getBookInfo(jsonResponse);

        JSONArray loanGrps = (JSONArray) jsonResponse.get("loanGrps");
        JSONArray keywords = (JSONArray) jsonResponse.get("keywords");
        JSONArray coLoanBooks = (JSONArray) jsonResponse.get("coLoanBooks");
        JSONArray maniaRecBooks = (JSONArray) jsonResponse.get("maniaRecBooks");
        JSONArray readerRecBooks = (JSONArray) jsonResponse.get("readerRecBooks");

        List<Top3LoanUserDto> top3LoanUserDtoList = new LinkedList<>();
        if(!loanGrps.isEmpty()){
            for (Object loanGrp : loanGrps) {
                JSONObject o = (JSONObject) ((JSONObject) loanGrp).get("loanGrp");
                top3LoanUserDtoList.add(Top3LoanUserDto.builder()
                        .age(o.getAsString("age"))
                        .gender(o.getAsString("gender"))
                        .loanCnt(o.getAsString("loanCnt"))
                        .ranking(o.getAsString("ranking"))
                        .build());
            }
            top3LoanUserDtoList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getRanking())));
            if(top3LoanUserDtoList.size() > 3) top3LoanUserDtoList = top3LoanUserDtoList.subList(0, 3); // 상위 3개만 추출
        }

        LinkedList<String> keywordList = new LinkedList<>();
        if(!keywords.isEmpty()){
            for(int i=0; i<10; i++) { //10개만 추출
                JSONObject o = (JSONObject) ((JSONObject) keywords.get(i)).get("keyword");
                keywordList.add(o.getAsString("word"));
            }
        }

        HashSet<String> duplicateCheckSet = new HashSet<>(); // 중복 체크
        LinkedList<CoLoanBooksDto> coLoanBooksList = new LinkedList<>();
        if(!coLoanBooks.isEmpty()){
            for (Object coLoanBook : coLoanBooks) {
                JSONObject o = (JSONObject) ((JSONObject) coLoanBook).get("book");
                String duplicateCheckKey = o.getAsString("bookname") + o.getAsString("authors");
                if (duplicateCheckSet.add(duplicateCheckKey)) { // 중복 확인
                    coLoanBooksList.add(CoLoanBooksDto.builder()
                            .bookname(o.getAsString("bookname"))
                            .authors(o.getAsString("authors"))
                            .publisher(o.getAsString("publisher"))
                            .publication_year(o.getAsString("publication_year"))
                            .isbn13(o.getAsString("isbn13"))
                            .vol(o.getAsString("vol"))
                            .loanCnt(o.getAsString("loanCnt"))
                            .build());
                }
            }
        }

        LinkedList<RecommendDto> recommendBooksList = new LinkedList<>();
        duplicateCheckSet = new HashSet<>(); // 중복 체크
        if(!maniaRecBooks.isEmpty()){
            for(Object maniaRecBook : maniaRecBooks) {
                JSONObject o = (JSONObject) ((JSONObject) maniaRecBook).get("book");
                String duplicateCheckKey = o.getAsString("bookname") + o.getAsString("authors");
                if (duplicateCheckSet.add(duplicateCheckKey)) { // 중복 확인
                    recommendBooksList.add(RecommendDto.builder()
                            .bookname(o.getAsString("bookname"))
                            .authors(o.getAsString("authors"))
                            .publisher(o.getAsString("publisher"))
                            .publication_year(o.getAsString("publication_year"))
                            .isbn13(o.getAsString("isbn13"))
                            .vol(o.getAsString("vol"))
                            .build());
                }
            }
        }

        if(!readerRecBooks.isEmpty()){
            for(Object readerRecBook : readerRecBooks) {
                JSONObject o = (JSONObject) ((JSONObject) readerRecBook).get("book");
                String duplicateCheckKey = o.getAsString("bookname") + o.getAsString("authors");
                if (duplicateCheckSet.add(duplicateCheckKey)) { // 중복 확인
                    recommendBooksList.add(RecommendDto.builder()
                            .bookname(o.getAsString("bookname"))
                            .authors(o.getAsString("authors"))
                            .publisher(o.getAsString("publisher"))
                            .publication_year(o.getAsString("publication_year"))
                            .isbn13(o.getAsString("isbn13"))
                            .vol(o.getAsString("vol"))
                            .build());
                }
            }
        }

        return DetailResponseDto.builder()
                .bookInfoDto(bookInfoDto)
                .top3LoanUserDtoList(top3LoanUserDtoList)
                .keywordDtoList(keywordList)
                .coLoanBooksDtoList(coLoanBooksList)
                .recommendResponseDtoList(recommendBooksList)
                .build();
    }

    public BookInfoDto getBookInfo(JSONObject jsonResponse) {
        JSONObject book = (JSONObject) jsonResponse.get("book");

        return BookInfoDto.builder()
                .bookname(book.getAsString("bookname"))
                .authors(book.getAsString("authors"))
                .publisher(book.getAsString("publisher"))
                .bookImageURL(book.getAsString("bookImageURL"))
                .description(book.getAsString("description"))
                .publication_year(book.getAsString("publication_year"))
                .isbn13(book.getAsString("isbn13"))
                .vol(book.getAsString("vol"))
                .class_no(book.getAsString("class_no"))
                .class_nm(book.getAsString("class_nm"))
                .loanCnt(book.getAsString("loanCnt")).build();
    }

    public LinkedList<LibSrchResponseDto> libSrch(JSONObject jsonResponse) {
        log.trace("ResponseParser > libSrch()");

        JSONArray libs = (JSONArray) jsonResponse.get("libs");

        LinkedList<LibSrchResponseDto> responseList = new LinkedList<>();

        if (libs == null) {
            return responseList;
        }

        for (Object o : libs) {
            JSONObject libsElement = (JSONObject) o;
            JSONObject lib = (JSONObject) libsElement.get("lib");

            responseList.add(LibSrchResponseDto.builder()
                    .libCode(lib.getAsString("libCode"))
                    .libName(lib.getAsString("libName"))
                    .address(lib.getAsString("address"))
                    .tel(lib.getAsString("tel"))
                    .fax(lib.getAsString("fax"))
                    .latitude(lib.getAsString("latitude"))
                    .longitude(lib.getAsString("longitude"))
                    .homepage(lib.getAsString("homepage"))
                    .closed(lib.getAsString("closed"))
                    .operatingTime(lib.getAsString("operatingTime"))
                    .BookCount(lib.getAsString("BookCount"))
                    .build());
        }
        return responseList;
    }

    public <T extends OpenAPIResponseInterface> LinkedList<T> customPageFilter(LinkedList<T> responseList, String filteredPageNo, String filteredPageSize) {
        log.trace("ResponseParser > customPageFilter()");

        int pageNo = Integer.parseInt(filteredPageNo);
        int pageSize = Integer.parseInt(filteredPageSize);

        int startIdx = (pageNo - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, responseList.size());

        try {
            return new LinkedList<>(responseList.subList(startIdx, endIdx));
        } catch (IllegalArgumentException e) {
            return new LinkedList<>();
        }

    }
}
