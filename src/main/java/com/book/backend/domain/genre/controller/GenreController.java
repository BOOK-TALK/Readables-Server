package com.book.backend.domain.genre.controller;

import com.book.backend.domain.book.entity.Book;
import com.book.backend.domain.genre.service.GenreService;
import com.book.backend.global.log.RequestLogger;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;

    /**
     * 대주제 KDC 번호(1자리) 입력 시 저장된 책 목록 리턴
     */
    @GetMapping("/books/main")
    public List<Book> findBooksByMainKdcNum(@RequestParam @Pattern(regexp = "\\d") Integer kdcNum) {
        RequestLogger.param(new String[]{"대주제 KDC 번호"}, kdcNum);

        List<Book> books = genreService.findBooksByMainKdcNum(kdcNum);
        return ResponseEntity.ok(books).getBody();
    }

    /**
     * 중주제 KDC 번호(2자리) 입력 시 저장된 책 목록 리턴
     */
    @GetMapping("/books/middle")
    public List<Book> findBooksBySubKdcNum(@RequestParam @Pattern(regexp = "\\d{2}") Integer kdcNum) {
        RequestLogger.param(new String[] {"중주제 KDC 번호"}, kdcNum);

        List<Book> books = genreService.findBooksBySubKdcNum(kdcNum);
        return ResponseEntity.ok(books).getBody();
    }

}
