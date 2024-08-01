package com.book.backend.domain.book.controller;

import com.book.backend.domain.auth.dto.SignupDto;
import com.book.backend.domain.book.dto.TestDto;
import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.global.log.RequestLogger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @GetMapping("/")
    public ResponseEntity<String> get(@RequestParam String name, int age) {
        RequestLogger.param("name", name);
        RequestLogger.param("age", age);

        return ResponseEntity.ok("Hi");
    }
    @PostMapping("/")
    public ResponseEntity<String> post(@RequestBody TestDto dto) {
        RequestLogger.body(dto);

        return ResponseEntity.ok("Hi");
    }
    @PostMapping("/both")
    public ResponseEntity<String> both(@RequestParam int grade, @RequestBody TestDto dto) {
        RequestLogger.param("grade", grade);
        RequestLogger.body(dto);

        return ResponseEntity.ok("Hi");
    }
}
