package com.book.backend.domain.book.controller;

import com.book.backend.domain.auth.dto.SignupDto;
import com.book.backend.domain.book.dto.TestDto;
import com.book.backend.domain.book.service.BookService;
import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.global.log.RequestLogger;
import jakarta.validation.Valid;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/get")
    public ResponseEntity<String> get(@RequestParam String name, int age) {
        RequestLogger.param(new String[] {"name", "age"}, name, age);

        bookService.logTest();
        return ResponseEntity.ok("Hi");
    }

    @PostMapping("/post")
    public ResponseEntity<String> post(@RequestBody TestDto dto) {
        RequestLogger.body(dto);
        bookService.logTest();
        return ResponseEntity.ok("Hi");
    }

    @PostMapping("/both")
    public ResponseEntity<String> both(@RequestParam int grade, @RequestBody TestDto dto) {
        RequestLogger.param(new String[]{"grade"}, grade);
        RequestLogger.body(dto);
        bookService.logTest();
        return ResponseEntity.ok("Hi");
    }
}
