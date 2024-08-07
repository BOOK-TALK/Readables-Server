package com.book.backend.domain.search.controller;

import com.book.backend.domain.book.service.BookService;
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


@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Slf4j
public class SearchController {
    private final SearchService searchService;
    private final ResponseTemplate responseTemplate;

    // 도서검색(16) API
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String input) throws Exception {
        RequestLogger.param(new String[]{"input"}, input);

        SearchRequestDto requestDto = SearchRequestDto.builder().title(input).build();
        LinkedList<SearchResponseDto> response = searchService.search(requestDto);

        return responseTemplate.success(response, HttpStatus.OK);
    }
}
