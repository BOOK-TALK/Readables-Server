package com.book.backend.domain.book.controller;

import com.book.backend.domain.book.service.BookService;
import com.book.backend.domain.openapi.dto.ManiaDto;
import com.book.backend.domain.openapi.dto.TestDto;
import java.net.HttpURLConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public ResponseEntity<HttpURLConnection> mania(@RequestParam String isbn) throws Exception {
        //TODO : RequestLogger
        bookService.mania(ManiaDto.builder().isbn13(isbn).build()); //TODO : 반환값
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() throws Exception {
        //TODO : RequestLogger
        bookService.test(TestDto.builder().pageSize(5).build()); //TODO : 반환값
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
