package com.book.backend.domain.genre.service;

import com.book.backend.domain.genre.entity.Genre;
import com.book.backend.domain.genre.repository.GenreRepository;
import com.book.backend.domain.openapi.dto.request.LoanItemSrchRequestDto;
import com.book.backend.domain.openapi.dto.response.LoanItemSrchResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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

    public LinkedList<LoanItemSrchResponseDto> periodToNowTrend(LoanItemSrchRequestDto requestDto, Integer dayPeriod, Integer maxSize) throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate startDt = today.minusDays(dayPeriod + 1);
        LocalDate endDt = today.minusDays(1);

        return periodTrend(requestDto, startDt, endDt, maxSize);
    }

    public LinkedList<LoanItemSrchResponseDto> thisWeekTrend(LoanItemSrchRequestDto requestDto, Integer maxSize) throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate startDt, endDt;
        // 월요일 또는 화요일이면 저번주로, 아니면 이번주로 계산
        if (today.getDayOfWeek() == DayOfWeek.MONDAY || today.getDayOfWeek() == DayOfWeek.TUESDAY) {
            startDt = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusDays(7);
            endDt = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).minusDays(7);
        } else {
            startDt = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            endDt = today.minusDays(1);
        }

        return periodTrend(requestDto, startDt, endDt, maxSize);
    }

    // periodToNowTrend, thisWeekTrend에 의해 호출됨
    public LinkedList<LoanItemSrchResponseDto> periodTrend(LoanItemSrchRequestDto requestDto, LocalDate startDt, LocalDate endDt, Integer maxSize) throws Exception {
        String subUrl = "loanItemSrch";

        requestDto.setStartDt(startDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        requestDto.setEndDt(endDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        JSONObject JsonResponse = openAPI.connect(subUrl, requestDto, new LoanItemSrchResponseDto());
        return new LinkedList<>(genreResponseParser.periodTrend(JsonResponse, maxSize));
    }

    public LinkedList<LoanItemSrchResponseDto> random(LoanItemSrchRequestDto requestDto, Integer maxSize) throws Exception {
        String subUrl = "loanItemSrch";

        requestDto.setPageSize("500");  // 셔플할 리스트의 페이지 크기 설정

        JSONObject JsonResponse = openAPI.connect(subUrl, requestDto, new LoanItemSrchResponseDto());

        return new LinkedList<>(genreResponseParser.random(JsonResponse, maxSize));
    }

    public LinkedList<LoanItemSrchResponseDto> newTrend(LoanItemSrchRequestDto requestDto, Integer maxSize) throws Exception {
        String subUrl = "loanItemSrch";

        requestDto.setPageSize("1500");  // 연도로 필터링하기 전 페이지 크기 설정
        LocalDate today = LocalDate.now();
        int currentYear = Integer.parseInt(today.format(DateTimeFormatter.ofPattern("yyyy")));

        JSONObject JsonResponse = openAPI.connect(subUrl, requestDto, new LoanItemSrchResponseDto());

        return new LinkedList<>(genreResponseParser.newTrend(JsonResponse, currentYear, maxSize));
    }

}
