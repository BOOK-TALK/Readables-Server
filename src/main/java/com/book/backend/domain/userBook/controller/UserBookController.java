package com.book.backend.domain.userBook.controller;

import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.domain.userBook.dto.UserBookDto;
import com.book.backend.domain.userBook.service.UserBookService;
import com.book.backend.global.log.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.book.backend.global.ResponseTemplate;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Slf4j
@Tag(name="읽은 책 & 책 찜", description = "읽은책 삭제 / 찜 해제 / 읽은 책 추가 / 찜 추가")
public class UserBookController {
    private final RequestValidate requestValidate;
    private final UserBookService userBookService;
    private final ResponseTemplate responseTemplate;

    @Operation(summary="읽은 책 추가", description="읽은 책 추가하고 user의 최종 읽은책 리스트를 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserBookDto.class)),
                    description = UserBookDto.description)})
    @PostMapping("/read")
    public ResponseEntity<?> setReadBooks(String isbn, String bookname, String bookImgUrl) {
        RequestLogger.param(new String[]{"isbn", "bookname", "bookImgUrl"}, isbn, bookname, bookImgUrl);
        requestValidate.isValidIsbn(isbn);
        List<UserBookDto> response = userBookService.setReadBooks(isbn, bookname, bookImgUrl);
        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 해제 API
    @Operation(summary="읽은 책 삭제", description="읽은 책 삭제하고 user의 최종 읽은책 리스트를 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserBookDto.class)),
                    description = UserBookDto.description)})
    @DeleteMapping("/read")
    public ResponseEntity<?> removeReadBooks(String isbn) {
        RequestLogger.param(new String[]{"isbn"}, isbn);
        requestValidate.isValidIsbn(isbn);
        List<UserBookDto> response = userBookService.removeReadBooks(isbn);
        return responseTemplate.success(response, HttpStatus.OK);
    }

    @Operation(summary="책 찜 추가", description="새로운 책을 찜하고 user의 최종 찜 리스트를 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserBookDto.class)),
                    description = UserBookDto.description)})
    @PostMapping("/dibs")
    public ResponseEntity<?> setDibsBooks(String isbn, String bookname, String bookImgUrl) throws Exception {
        requestValidate.isValidIsbn(isbn);
        List<UserBookDto> response = userBookService.setDibsBooks(isbn, bookname, bookImgUrl);
        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 해제 API
    @Operation(summary="책 찜 해제", description="찜 해제하고 user의 최종 찜 리스트를 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserBookDto.class)),
                    description = UserBookDto.description)})
    @DeleteMapping("/dibs")
    public ResponseEntity<?> removeDibsBooks(String isbn) {
        requestValidate.isValidIsbn(isbn);
        List<UserBookDto> response = userBookService.removeDibsBooks(isbn);
        return responseTemplate.success(response, HttpStatus.OK);
    }
}
