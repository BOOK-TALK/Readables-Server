package com.book.backend.domain.search.controller;

import com.book.backend.domain.book.service.RequestValidate;
import com.book.backend.domain.openapi.dto.request.SearchRequestDto;
import com.book.backend.domain.openapi.dto.response.SearchResponseDto;
import com.book.backend.domain.search.service.SearchService;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;


@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Slf4j
public class SearchController {
    private final SearchService searchService;
    private final RequestValidate requestValidate;
    private final ResponseTemplate responseTemplate;

    // 도서검색(16) API
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String title, String libCode, int pageNo, int pageSize) throws Exception {
        RequestLogger.param(new String[]{"title, libCode, pageNo, pageSize"}, title, libCode, pageNo, pageSize);
        requestValidate.isValidLibCode(libCode);
        requestValidate.isValidPageNum(pageNo);
        requestValidate.isValidPageNum(pageSize);
        SearchRequestDto requestDto = SearchRequestDto.builder()
                .keyword(UriUtils.encode("\"" + title + "\"", "UTF-8")) // 공백 및 한글 인코딩
                .pageNo(pageNo).pageSize(pageSize).build();
        LinkedList<SearchResponseDto> response = searchService.search(requestDto);

        searchService.setLoanAvailable(response, libCode);
        response = searchService.duplicateChecker(response);

        return responseTemplate.success(response, HttpStatus.OK);
    }
}
