package com.book.backend.domain.genre.controller;

import com.book.backend.domain.genre.service.GenreService;
import com.book.backend.domain.openapi.dto.request.LoanItemSrchRequestDto;
import com.book.backend.domain.openapi.dto.response.LoanItemSrchResponseDto;
import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    // 일주일 인기순
    @Operation(summary = "일주일 인기순", description = "장르 코드(2자리)를 입력받아 1주일 인기순 도서 리스트를 반환합니다.",
            parameters = {
                    @Parameter(name = "genreCode", description = "세부 장르 코드 (필수, figma 참고)"),
                    @Parameter(name = "pageNo", description = "페이지 번호 (필수)"),
                    @Parameter(name = "pageSize", description = "페이지 당 요소 수 (필수)")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoanItemSrchResponseDto.class)),
                    description = LoanItemSrchResponseDto.description)})
    @GetMapping("/aWeekTrend")
    public ResponseEntity<?> aWeekTrend(@RequestParam String genreCode,
                                        @RequestParam String pageNo,
                                        @RequestParam String pageSize) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, genreCode);
        requestValidate.isValidGenreCode(genreCode);

        LoanItemSrchRequestDto requestDto = LoanItemSrchRequestDto.builder()
                .dtl_kdc(genreCode)
                .build();
        LinkedList<LoanItemSrchResponseDto> response = genreService.periodToNowTrend(requestDto, 7, pageNo, pageSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 한 달 인기순
    @Operation(summary = "한 달 인기순", description = "장르 코드(2자리)를 입력받아 한 달 인기순 도서 리스트를 반환합니다.",
            parameters = {
                    @Parameter(name = "genreCode", description = "세부 장르 코드 (필수, figma 참고)"),
                    @Parameter(name = "pageNo", description = "페이지 번호 (필수)"),
                    @Parameter(name = "pageSize", description = "페이지 당 요소 수 (필수)")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoanItemSrchResponseDto.class)),
                    description = LoanItemSrchResponseDto.description)})
    @GetMapping("/aMonthTrend")
    public ResponseEntity<?> aMonthTrend(@RequestParam String genreCode,
                                         @RequestParam String pageNo,
                                         @RequestParam String pageSize) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, genreCode);
        requestValidate.isValidGenreCode(genreCode);

        LoanItemSrchRequestDto requestDto = LoanItemSrchRequestDto.builder()
                .dtl_kdc(genreCode)
                .build();
        LinkedList<LoanItemSrchResponseDto> response = genreService.periodToNowTrend(requestDto, 30, pageNo, pageSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 이번 주 인기 도서 (N월 M주차)
    @Operation(summary = "이번 주 인기 도서 (N월 M주차)", description = "장르 코드(2자리)를 입력받아 이번 주(N월 M주차) 인기 도서 목록을 반환합니다. " +
            "단, 월요일 또는 화요일이면 저번 주차로, 아니면 이번 주차로 계산됩니다.",
            parameters = {
                    @Parameter(name = "genreCode", description = "세부 장르 코드 (필수, figma 참고)"),
                    @Parameter(name = "pageNo", description = "페이지 번호 (필수)"),
                    @Parameter(name = "pageSize", description = "페이지 당 요소 수 (필수)")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoanItemSrchResponseDto.class)),
                    description = LoanItemSrchResponseDto.description)})
    @GetMapping("/thisWeekTrend")
    public ResponseEntity<?> thisWeekTrend(@RequestParam String genreCode,
                                           @RequestParam String pageNo,
                                           @RequestParam String pageSize) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, genreCode);
        requestValidate.isValidGenreCode(genreCode);

        LoanItemSrchRequestDto requestDto = LoanItemSrchRequestDto.builder()
                .dtl_kdc(genreCode)
                .build();
        LinkedList<LoanItemSrchResponseDto> response = genreService.thisWeekTrend(requestDto, pageNo, pageSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 무작위순
    @Operation(summary = "무작위순", description = "장르 코드(2자리)를 입력받아 무작위순 도서 리스트를 반환합니다. " +
            "호출할 때마다 다른 리스트가 반환됩니다.",
            parameters = {
                    @Parameter(name = "genreCode", description = "세부 장르 코드 (필수, figma 참고)"),
                    @Parameter(name = "maxSize", description = "리스트 길이 제한 (선택)")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoanItemSrchResponseDto.class)),
                    description = LoanItemSrchResponseDto.description)})
    @GetMapping("/random")
    public ResponseEntity<?> random(@RequestParam String genreCode,
                                    @RequestParam(required = false) Integer maxSize) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, genreCode);
        requestValidate.isValidGenreCode(genreCode);

        LoanItemSrchRequestDto requestDto = LoanItemSrchRequestDto.builder()
                .dtl_kdc(genreCode)
                .build();
        LinkedList<LoanItemSrchResponseDto> response = genreService.random(requestDto, maxSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 신작 인기순
    @Operation(summary = "신작 인기순", description = "장르 코드(2자리)를 입력받아 최근 2년 내 출판된 인기 도서 리스트를 반환합니다.",
            parameters = {
                    @Parameter(name = "genreCode", description = "세부 장르 코드 (필수, figma 참고)"),
                    @Parameter(name = "pageNo", description = "페이지 번호 (필수)"),
                    @Parameter(name = "pageSize", description = "페이지 당 요소 수 (필수)")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoanItemSrchResponseDto.class)),
                    description = LoanItemSrchResponseDto.description)})
    @GetMapping("/newTrend")
    public ResponseEntity<?> newTrend(@RequestParam String genreCode,
                                      @RequestParam String pageNo,
                                      @RequestParam String pageSize) throws Exception {
        RequestLogger.param(new String[]{"kdcNum"}, genreCode);
        requestValidate.isValidGenreCode(genreCode);

        LoanItemSrchRequestDto requestDto = LoanItemSrchRequestDto.builder()
                .dtl_kdc(genreCode)
                .build();
        LinkedList<LoanItemSrchResponseDto> response = genreService.newTrend(requestDto, pageNo, pageSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }

}
