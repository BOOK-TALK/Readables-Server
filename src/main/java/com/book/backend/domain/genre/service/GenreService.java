package com.book.backend.domain.genre.service;

import com.book.backend.domain.book.entity.Book;
import com.book.backend.domain.genre.entity.Genre;
import com.book.backend.domain.genre.repository.GenreRepository;
import com.book.backend.domain.openapi.dto.request.genre.NewTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.genre.PeriodTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.genre.RandomRequestDto;
import com.book.backend.domain.openapi.dto.response.genre.NewTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.genre.PeriodTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.genre.RandomResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {
    private final GenreRepository genreRepository;
    private final OpenAPI openAPI;

    public Genre findById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid genre Id:" + id));
    }

    @Transactional
    public void addBook(Genre genre, Book book) {
        if (genre.getLevel() == 2) {
            genre.getBooks().add(book);
            if (book.getGenre() != genre) {
                book.setGenre(genre);
            }
        } else {
            throw new UnsupportedOperationException("2단계 장르만 책 리스트를 가질 수 있습니다.");
        }
    }

    public List<Genre> findSubGenresByKdcNum(String kdcNum) {
        Optional<Genre> genre = genreRepository.findByKdcNum(kdcNum);
        return genre.map(Genre::getSubGenres).orElse(null);
    }

    public Genre findByMainKdcNumAndSubKdcNum(String mainKdcNum, String subKdcNum) {
        return genreRepository.findByParentGenreKdcNumAndKdcNum(mainKdcNum, subKdcNum)
                .orElseThrow(() -> new IllegalArgumentException("KDC 번호가" + mainKdcNum + subKdcNum + "인 장르를 찾을 수 없습니다."));
    }

    public List<Book> findBooksByMainKdcNum(Integer kdcNum) {
        String mainKdcNum = String.valueOf(kdcNum);

        List<Book> allBooks = new ArrayList<>();
        List<Genre> subGenres = findSubGenresByKdcNum(mainKdcNum);

        if (subGenres != null) {
            for (Genre subGenre : subGenres) {
                if (subGenre.getBooks() != null) {
                    allBooks.addAll(subGenre.getBooks());
                }
            }
        }

        return allBooks;
    }

    public List<Book> findBooksBySubKdcNum(Integer kdcNum) {
        String mainKdcNum = String.valueOf(kdcNum / 10);
        String subKdcNum = String.valueOf(kdcNum % 10);

        Genre findGenre = findByMainKdcNumAndSubKdcNum(mainKdcNum, subKdcNum);
        return findGenre.getBooks();
    }

    public LinkedList<PeriodTrendResponseDto> periodTrend(PeriodTrendRequestDto requestDto, Integer dayPeriod, Integer maxSize) throws Exception {
        String subUrl = "loanItemSrch";

        LocalDate today = LocalDate.now();
        requestDto.setStartDt(today.minusDays(dayPeriod + 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        requestDto.setEndDt(today.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        JSONObject JsonResponse = openAPI.connect(subUrl, requestDto, new PeriodTrendResponseDto());
        ResponseParser responseParser = new ResponseParser();

        return new LinkedList<>(responseParser.periodTrend(JsonResponse, maxSize));
    }

    public LinkedList<RandomResponseDto> random(RandomRequestDto requestDto, Integer maxSize) throws Exception {
        String subUrl = "loanItemSrch";
        int resultSize = 200;

        requestDto.setPageSize(500);  // 셔플할 리스트의 페이지 크기 설정

        JSONObject JsonResponse = openAPI.connect(subUrl, requestDto, new RandomResponseDto());
        ResponseParser responseParser = new ResponseParser();

        return new LinkedList<>(responseParser.random(JsonResponse, resultSize, maxSize));
    }

    public LinkedList<NewTrendResponseDto> newTrend(NewTrendRequestDto requestDto, Integer maxSize) throws Exception {
        String subUrl = "loanItemSrch";

        requestDto.setPageSize(1500);  // 연도로 필터링하기 전 페이지 크기 설정
        LocalDate today = LocalDate.now();
        int currentYear = Integer.parseInt(today.format(DateTimeFormatter.ofPattern("yyyy")));

        JSONObject JsonResponse = openAPI.connect(subUrl, requestDto, new NewTrendResponseDto());
        ResponseParser responseParser = new ResponseParser();

        return new LinkedList<>(responseParser.newTrend(JsonResponse, currentYear, maxSize));
    }

}
