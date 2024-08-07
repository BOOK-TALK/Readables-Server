package com.book.backend.domain.genre.service;

import com.book.backend.domain.genre.entity.Genre;
import com.book.backend.domain.genre.repository.GenreRepository;
import com.book.backend.domain.openapi.dto.request.LoanTrendRequestDto;
import com.book.backend.domain.openapi.dto.response.LoanTrendResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {
    private final GenreRepository genreRepository;
    private final OpenAPI openAPI;
    private final GenreResponseParser genreResponseParser;

    public Genre findById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid genre Id:" + id));
    }

    public List<Genre> findSubGenresByKdcNum(String kdcNum) {
        Optional<Genre> genre = genreRepository.findByKdcNum(kdcNum);
        return genre.map(Genre::getSubGenres).orElse(null);
    }

    public Genre findByMainKdcNumAndSubKdcNum(String mainKdcNum, String subKdcNum) {
        return genreRepository.findByParentGenreKdcNumAndKdcNum(mainKdcNum, subKdcNum)
                .orElseThrow(() -> new IllegalArgumentException("KDC 번호가" + mainKdcNum + subKdcNum + "인 장르를 찾을 수 없습니다."));
    }

    public LinkedList<LoanTrendResponseDto> periodTrend(LoanTrendRequestDto requestDto, Integer dayPeriod, Integer maxSize) throws Exception {
        String subUrl = "loanItemSrch";

        LocalDate today = LocalDate.now();
        requestDto.setStartDt(today.minusDays(dayPeriod + 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        requestDto.setEndDt(today.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        JSONObject JsonResponse = openAPI.connect(subUrl, requestDto, new LoanTrendResponseDto());

        return new LinkedList<>(genreResponseParser.periodTrend(JsonResponse, maxSize));
    }

    public LinkedList<LoanTrendResponseDto> random(LoanTrendRequestDto requestDto, Integer maxSize) throws Exception {
        String subUrl = "loanItemSrch";
        int resultSize = 200;

        requestDto.setPageSize(500);  // 셔플할 리스트의 페이지 크기 설정

        JSONObject JsonResponse = openAPI.connect(subUrl, requestDto, new LoanTrendResponseDto());

        return new LinkedList<>(genreResponseParser.random(JsonResponse, resultSize, maxSize));
    }

    public LinkedList<LoanTrendResponseDto> newTrend(LoanTrendRequestDto requestDto, Integer maxSize) throws Exception {
        String subUrl = "loanItemSrch";

        requestDto.setPageSize(1500);  // 연도로 필터링하기 전 페이지 크기 설정
        LocalDate today = LocalDate.now();
        int currentYear = Integer.parseInt(today.format(DateTimeFormatter.ofPattern("yyyy")));

        JSONObject JsonResponse = openAPI.connect(subUrl, requestDto, new LoanTrendResponseDto());

        return new LinkedList<>(genreResponseParser.newTrend(JsonResponse, currentYear, maxSize));
    }

}
