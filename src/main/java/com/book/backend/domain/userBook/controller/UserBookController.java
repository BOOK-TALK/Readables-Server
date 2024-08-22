package com.book.backend.domain.userBook.controller;

import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.domain.userBook.dto.UserBookDto;
import com.book.backend.domain.userBook.service.UserBookService;
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
@RequestMapping("/api/book/dibs")
@RequiredArgsConstructor
@Slf4j
@Tag(name="책 찜", description = "찜 추가 / 찜 해제")
public class UserBookController {
    private final RequestValidate requestValidate;
    private final UserBookService userBookService;
    private final ResponseTemplate responseTemplate;

    @Operation(summary="책 찜 추가", description="새로운 책을 찜하고 user의 최종 찜 리스트를 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserBookDto.class)),
                    description = UserBookDto.description)})
    @PostMapping("/")
    public ResponseEntity<?> setDibs(String isbn, String bookname, String bookImgUrl) throws Exception {
        requestValidate.isValidIsbn(isbn);
        List<UserBookDto> response = userBookService.setDibs(isbn, bookname, bookImgUrl);
        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 해제 API
    @Operation(summary="책 찜 해제", description="찜 해제하고 user의 최종 찜 리스트를 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserBookDto.class)),
                    description = UserBookDto.description)})
    @DeleteMapping("/")
    public ResponseEntity<?> removeDibs(String isbn) {
        requestValidate.isValidIsbn(isbn);
        List<UserBookDto> response = userBookService.removeDibs(isbn);
        return responseTemplate.success(response, HttpStatus.OK);
    }
}
