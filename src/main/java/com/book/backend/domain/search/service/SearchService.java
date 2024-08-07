package com.book.backend.domain.search.service;

import com.book.backend.domain.openapi.dto.request.BookExistRequestDto;
import com.book.backend.domain.openapi.dto.request.SearchRequestDto;
import com.book.backend.domain.openapi.dto.response.BookExistResponseDto;
import com.book.backend.domain.openapi.dto.response.SearchResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import java.util.HashSet;
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
public class SearchService {
    private final OpenAPI openAPI;

    public LinkedList<SearchResponseDto> search(SearchRequestDto requestDto) throws Exception {
        log.trace("search()");
        String subUrl = "srchBooks";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new SearchResponseDto());
        ResponseParser responseParser = new ResponseParser();
        return responseParser.search(jsonResponse);
    }

    public void setLoanAvailable(LinkedList<SearchResponseDto> responseList, String libCode) throws Exception {
        log.trace("setLoanAvailable()");

        // 모든 책에 대해 대출 가능 여부 설정
        for (SearchResponseDto dto : responseList) {
            BookExistRequestDto bookExistRequestDto = BookExistRequestDto.builder()
                    .libCode(libCode)
                    .isbn13(dto.getIsbn13()).build();
            String subUrl = "bookExist";
            JSONObject jsonResponse = openAPI.connect(subUrl, bookExistRequestDto, new BookExistResponseDto());
            ResponseParser responseParser = new ResponseParser();

            dto.setLoanAvailable(responseParser.loanAvailable(jsonResponse));
        }
    }

    public LinkedList<SearchResponseDto> duplicateChecker(LinkedList<SearchResponseDto> list){
        log.trace("duplicateChecker()");
        LinkedList<SearchResponseDto> duplicateRemovedList = new LinkedList<>();
        HashSet<String> set = new HashSet<>();
        for(SearchResponseDto dto : list){
            String key = dto.getBookname() + dto.getAuthors();
            if(set.add(key)) duplicateRemovedList.add(dto);
        }
        return duplicateRemovedList;
    }
}
