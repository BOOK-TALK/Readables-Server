package com.book.backend.domain.detail.controller;

import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.domain.detail.service.DetailService;
import com.book.backend.domain.openapi.dto.request.DetailRequestDto;
import com.book.backend.domain.openapi.dto.response.DetailResponseDto;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class DetailController {
    private final DetailService detailService;
    private final RequestValidate requestValidate;
    private final ResponseTemplate responseTemplate;

    // 도서 상세 (8) API
    @GetMapping("/detail")
    public ResponseEntity<?> search(@RequestParam String isbn) throws Exception {
        RequestLogger.param(new String[]{"isbn"}, isbn);
        requestValidate.isValidIsbn(isbn);

        DetailRequestDto requestDto = DetailRequestDto.builder().isbn13(isbn).build();
        LinkedList<DetailResponseDto> response = detailService.detail(requestDto);

        return responseTemplate.success(response, HttpStatus.OK);
    }
}
