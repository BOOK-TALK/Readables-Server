package com.book.backend.domain.genre.controller;

import com.book.backend.domain.genre.service.GenreService;
import com.book.backend.domain.openapi.dto.request.LoanTrendRequestDto;
import com.book.backend.domain.openapi.dto.response.LoanTrendResponseDto;
import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

@RestController
@RequestMapping("/api/genre")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;
    private final RequestValidate requestValidate;
    private final ResponseTemplate responseTemplate;

    /**
     * 일주일 인기순 - 중주제 KDC 번호(2자리) 입력 시 1주일 인기순 도서 목록 리턴
     */
    @GetMapping("/aWeekTrend")
    public ResponseEntity<?> aWeekTrend(@RequestParam String subKdc,
                                        @RequestParam(required = false) Integer maxSize) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, subKdc);
        requestValidate.isValidGenreCode(subKdc);

        LoanTrendRequestDto requestDto = LoanTrendRequestDto.builder().dtl_kdc(subKdc).build();
        LinkedList<LoanTrendResponseDto> response = genreService.periodToNowTrend(requestDto, 7, maxSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    /**
     * 한달 인기순 - 중주제 KDC 번호(2자리) 입력 시 1개월 인기순 도서 목록 리턴
     */
    @GetMapping("/aMonthTrend")
    public ResponseEntity<?> aMonthTrend(@RequestParam String subKdc,
                                         @RequestParam(required = false) Integer maxSize) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, subKdc);
        requestValidate.isValidGenreCode(subKdc);

        LoanTrendRequestDto requestDto = LoanTrendRequestDto.builder().dtl_kdc(subKdc).build();
        LinkedList<LoanTrendResponseDto> response = genreService.periodToNowTrend(requestDto, 30, maxSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    /**
     * N월 M주차 - 중주제 KDC 번호(2자리) 입력시 N월 M주차 인기 도서 목록 리턴
     *           - (월요일 또는 화요일이면 저번 주로, 아니면 이번 주로 계산)
     */
    @GetMapping("/thisWeekTrend")
    public ResponseEntity<?> thisWeekTrend(@RequestParam String subKdc,
                                           @RequestParam(required = false) Integer maxSize) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, subKdc);
        requestValidate.isValidGenreCode(subKdc);

        LoanTrendRequestDto requestDto = LoanTrendRequestDto.builder().dtl_kdc(subKdc).build();
        LinkedList<LoanTrendResponseDto> response = genreService.thisWeekTrend(requestDto, maxSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    /**
     * 무작위순 - 중주제 KDC 번호(2자리) 입력 시 랜덤한 도서 목록 리턴
     */
    @GetMapping("/random")
    public ResponseEntity<?> random(@RequestParam String subKdc,
                                    @RequestParam(required = false) Integer maxSize) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, subKdc);
        requestValidate.isValidGenreCode(subKdc);

        LoanTrendRequestDto requestDto = LoanTrendRequestDto.builder().dtl_kdc(subKdc).build();
        LinkedList<LoanTrendResponseDto> response = genreService.random(requestDto, maxSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    /**
     * 신작 인기순 - 중주제 KDC 번호(2자리) 입력 시 3년 내 출판된 인기 도서 목록 리턴
     */
    @GetMapping("/newTrend")
    public ResponseEntity<?> newTrend(@RequestParam String subKdc,
                                      @RequestParam(required = false) Integer maxSize) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, subKdc);
        requestValidate.isValidGenreCode(subKdc);

        LoanTrendRequestDto requestDto = LoanTrendRequestDto.builder().dtl_kdc(subKdc).build();
        LinkedList<LoanTrendResponseDto> response = genreService.newTrend(requestDto, maxSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }

}
