package com.book.backend.domain.book.controller;

import com.book.backend.domain.book.service.BookRequestValidate;
import com.book.backend.domain.book.service.BookService;
import com.book.backend.domain.openapi.dto.request.HotTrendRequestDto;
import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.RecommendResponseDto;
import com.book.backend.domain.openapi.dto.request.RecommendRequestDto;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import java.util.HashSet;
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
public class BookController {
    private final BookService bookService;
    private final BookRequestValidate bookRequestValidate;
    private final ResponseTemplate responseTemplate;

    // 마니아(4), 다독자(5) 추천 API
    @GetMapping("/recommend")
    public ResponseEntity<?> recommend(@RequestParam(required = true) String isbn) throws Exception {
        RequestLogger.param(new String[]{"isbn"}, isbn);
        bookRequestValidate.isValidIsbn(isbn);

        RecommendRequestDto requestDto = RecommendRequestDto.builder().isbn13(isbn).build();
        LinkedList<RecommendResponseDto> response = bookService.recommend(requestDto);

        HashSet<String> duplicateCheckSet = new HashSet<>();
        LinkedList<RecommendResponseDto> duplicateRemovedList = bookService.duplicateChecker(response, duplicateCheckSet);
        bookService.ensureRecommendationsCount(duplicateRemovedList, duplicateCheckSet);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 대출급상승(12) API
    @GetMapping("/hotTrend")
    public ResponseEntity<?> hotTrend(@RequestParam(required = true) String searchDt) throws Exception {
        RequestLogger.param(new String[]{"searchDt"}, searchDt);
        bookRequestValidate.isValidSearchDt(searchDt);

        HotTrendRequestDto requestDto = HotTrendRequestDto.builder().searchDt(searchDt).build();
        LinkedList<HotTrendResponseDto> response = bookService.hotTrend(requestDto);

        return responseTemplate.success(response, HttpStatus.OK);
    }
}
