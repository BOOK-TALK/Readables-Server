package com.book.backend.domain.book.controller;

import com.book.backend.domain.book.service.BookService;
import com.book.backend.domain.openapi.dto.request.HotTrendRequestDto;
import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.ManiaResponseDto;
import com.book.backend.domain.openapi.dto.response.OpenAPIResponseInterface;
import com.book.backend.domain.openapi.dto.request.ManiaRequestDto;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
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
    @Autowired
    BookService bookService;

    @GetMapping("/mania")
    public ResponseEntity<LinkedList<ManiaResponseDto>> mania(@RequestParam String isbn) throws Exception {
        ManiaRequestDto requestDto = ManiaRequestDto.builder().isbn13(isbn).build();
        LinkedList<ManiaResponseDto> response = bookService.mania(requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/hotTrend")
    public ResponseEntity<LinkedList<HotTrendResponseDto>>hotTrend(@RequestParam String searchDt) throws Exception {
        HotTrendRequestDto requestDto = HotTrendRequestDto.builder().searchDt(searchDt).build();
        LinkedList<HotTrendResponseDto> response = bookService.hotTrend(requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
