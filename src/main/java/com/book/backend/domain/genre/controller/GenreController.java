package com.book.backend.domain.genre.controller;

import com.book.backend.domain.book.service.BookRequestValidate;
import com.book.backend.domain.genre.service.GenreRequestValidate;
import com.book.backend.domain.genre.service.GenreService;
import com.book.backend.domain.openapi.dto.request.genre.NewTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.genre.PeriodTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.genre.RandomRequestDto;
import com.book.backend.domain.openapi.dto.response.genre.NewTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.genre.PeriodTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.genre.RandomResponseDto;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Random;

@RestController
@RequestMapping("/api/genre")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;
    private final GenreRequestValidate genreRequestValidate;
    private final ResponseTemplate responseTemplate;

//    /**
//     * 대주제 KDC 번호(1자리) 입력 시 저장된 책 목록 리턴
//     */
//    @GetMapping("/main")
//    public List<Book> findBooksByMainKdcNum(@RequestParam @Pattern(regexp = "\\d") Integer kdcNum) {
//        RequestLogger.param(new String[]{"대주제 KDC 번호"}, kdcNum);
//
//        List<Book> books = genreService.findBooksByMainKdcNum(kdcNum);
//        return ResponseEntity.ok(books).getBody();
//    }
//
//    /**
//     * 중주제 KDC 번호(2자리) 입력 시 저장된 책 목록 리턴
//     */
//    @GetMapping("/sub")
//    public List<Book> findBooksBySubKdcNum(@RequestParam @Pattern(regexp = "\\d{2}") Integer kdcNum) {
//        RequestLogger.param(new String[] {"중주제 KDC 번호"}, kdcNum);
//
//        List<Book> books = genreService.findBooksBySubKdcNum(kdcNum);
//        return ResponseEntity.ok(books).getBody();
//    }

    /**
     * 일주일 인기순 - 중주제 KDC 번호(2자리) 입력 시 1주일 인기순 도서 목록 리턴
     */
    @GetMapping("/aWeekTrend")
    public ResponseEntity<?> aWeekTrend(@RequestParam(required = true) String subKdc) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, subKdc);
        genreRequestValidate.isValidSubKdc(subKdc);

        PeriodTrendRequestDto requestDto = PeriodTrendRequestDto.builder().dtl_kdc(subKdc).build();
        LinkedList<PeriodTrendResponseDto> response = genreService.periodTrend(requestDto, 7);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    /**
     * 한달 인기순 - 중주제 KDC 번호(2자리) 입력 시 1개월 인기순 도서 목록 리턴
     */
    @GetMapping("/aMonthTrend")
    public ResponseEntity<?> aMonthTrend(@RequestParam(required = true) String subKdc) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, subKdc);
        genreRequestValidate.isValidSubKdc(subKdc);

        PeriodTrendRequestDto requestDto = PeriodTrendRequestDto.builder().dtl_kdc(subKdc).build();
        LinkedList<PeriodTrendResponseDto> response = genreService.periodTrend(requestDto, 30);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    /**
     * 무작위순 - 중주제 KDC 번호(2자리) 입력 시 랜덤한 도서 목록 리턴
     */
    @GetMapping("/random")
    public ResponseEntity<?> random(@RequestParam(required = true) String subKdc) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, subKdc);
        genreRequestValidate.isValidSubKdc(subKdc);

        RandomRequestDto requestDto = RandomRequestDto.builder().dtl_kdc(subKdc).build();
        LinkedList<RandomResponseDto> response = genreService.random(requestDto);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    /**
     * 신작 인기순 - 중주제 KDC 번호(2자리) 입력 시 3년 내 출판된 인기 도서 목록 리턴
     */
    @GetMapping("/newTrend")
    public ResponseEntity<?> newTrend(@RequestParam(required = true) String subKdc) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, subKdc);
        genreRequestValidate.isValidSubKdc(subKdc);

        NewTrendRequestDto requestDto = NewTrendRequestDto.builder().dtl_kdc(subKdc).build();
        LinkedList<NewTrendResponseDto> response = genreService.newTrend(requestDto);

        return responseTemplate.success(response, HttpStatus.OK);
    }

}
