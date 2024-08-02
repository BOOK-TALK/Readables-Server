package com.book.backend.domain.genre.controller;

import com.book.backend.domain.book.entity.Book;
import com.book.backend.domain.genre.service.GenreService;
import com.book.backend.global.log.RequestLogger;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;

    /**
     * KDC 번호(2자리) 입력 시 저장된 책 목록 리턴
     */
    @GetMapping("/books")
    public List<Book> getBooksByTwoDigitsKdcNum(@RequestParam @Pattern(regexp = "\\d{2}") Integer kdcNum) {
        RequestLogger.param(new String[] {"2자리 KDC 번호"}, kdcNum);

        List<Book> books = genreService.getBooksByTwoDigitsKdcNum(kdcNum);
        return ResponseEntity.ok(books).getBody();
    }

}
