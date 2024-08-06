package com.book.backend.domain.book.controller;

import com.book.backend.domain.book.service.BookService;
import com.book.backend.domain.book.service.RequestValidate;
import com.book.backend.domain.openapi.dto.request.CustomHotTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.HotTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.KeywordRequestDto;
import com.book.backend.domain.openapi.dto.response.CustomHotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.KeywordResponseDto;
import com.book.backend.domain.openapi.dto.response.RecommendResponseDto;
import com.book.backend.domain.openapi.dto.request.RecommendRequestDto;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import java.util.HashSet;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final RequestValidate requestValidate;
    private final ResponseTemplate responseTemplate;

    // 마니아(4), 다독자(5) 추천 API
    @GetMapping("/recommend")
    public ResponseEntity<?> recommend(@RequestParam(required = true) String isbn) throws Exception {
        RequestLogger.param(new String[]{"isbn"}, isbn);
        requestValidate.isValidIsbn(isbn);

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
        requestValidate.isValidSearchDt(searchDt);

        HotTrendRequestDto requestDto = HotTrendRequestDto.builder().searchDt(searchDt).build();
        LinkedList<HotTrendResponseDto> response = bookService.hotTrend(requestDto);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 지난달 키워드 (17)
    @GetMapping("/keyword")
    public ResponseEntity<?> keywords() throws Exception {
        KeywordRequestDto requestDto = new KeywordRequestDto();
        requestDto.setSearchDt();

        LinkedList<KeywordResponseDto> response = bookService.keywords(requestDto);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 조건형 인기 대출 도서 (3)
    @GetMapping("/customHotTrend")
    public ResponseEntity<?> customHotTrend(@RequestParam(required = false) String weekMonth,
                                            @RequestParam(required = false) String peerAge,
                                            @RequestParam(required = false) String ageRange,
                                            @RequestParam(required = false) String gender,
                                            @RequestParam(required = false) String genreCode,
                                            @RequestParam(required = false) String region,
                                            @RequestParam(required = false) String libCode) throws Exception {
        requestValidate.validCustomHotTrendRequest(weekMonth, peerAge, ageRange, gender, genreCode, region, libCode);

        // TODO : 입력값에 맞게 dto 에 값 넣는 로직.
//        CustomHotTrendRequestDto requestDto = CustomHotTrendRequestDto.builder().;

//        LinkedList<CustomHotTrendResponseDto> response = bookService.customHotTrend(requestDto);

        return responseTemplate.success(null, HttpStatus.OK);
    }
}
