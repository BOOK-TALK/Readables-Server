package com.book.backend.domain.library.controller;

import com.book.backend.domain.library.service.LibraryService;
import com.book.backend.domain.openapi.dto.request.LibSrchRequestDto;
import com.book.backend.domain.openapi.dto.response.LibSrchResponseDto;
import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.global.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
@Slf4j
@Tag(name="도서관 검색", description = "지역코드 기반 도서관 검색 / libcode 기반 도서관 검색")
public class LibraryController {
    private final RequestValidate requestValidate;
    private final LibraryService libraryService;
    private final ResponseTemplate responseTemplate;

    // 지역코드 기반 도서관 검색
    @Operation(summary = "지역코드 기반 도서관 검색", description = "지역 코드를 입력받아 해당 지역의 정보 공개 도서관 리스트를 반환합니다.",
            parameters = {
                    @Parameter(name = "regionCode", description = "대지역 코드 (필수)"),
                    @Parameter(name = "regionDetailCode", description = "세부지역 코드 (선택)")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LibSrchResponseDto.class)),
                    description = LibSrchResponseDto.description)})
    @GetMapping("/searchByRegion")
    public ResponseEntity<?> searchLibrariesByRegion(@RequestParam String regionCode,
                                                     @RequestParam(required = false) String regionDetailCode) throws Exception {
        log.trace("LibraryController > searchLibrariesByRegion");

        requestValidate.isValidRegionCode(regionCode);
        if (regionDetailCode != null) {
            requestValidate.isValidRegionDetailCode(regionDetailCode);
        }

        LibSrchRequestDto requestDto;

        if (regionDetailCode != null) {
            requestDto = LibSrchRequestDto.builder()
                    .region(regionCode)
                    .dtl_region(regionDetailCode)
                    .build();
        } else {
            requestDto = LibSrchRequestDto.builder()
                    .region(regionCode)
                    .build();
        }

        LinkedList<LibSrchResponseDto> response = libraryService.searchLibraries(requestDto);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 도서관 코드 기반 도서관 검색
    @Operation(summary = "도서관 코드 기반 도서관 검색", description = "도서관 코드를 입력받아 해당 도서관의 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "libCode", description = "도서관 코드 (필수)")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LibSrchResponseDto.class)),
                    description = LibSrchResponseDto.description)})
    @GetMapping("/searchByLibCode")
    public ResponseEntity<?> searchLibraryByLibCode(@RequestParam String libCode) throws Exception {
        log.trace("LibraryController > searchLibraryByLibCode");

        LibSrchRequestDto requestDto = LibSrchRequestDto.builder()
                .libCode(libCode)
                .build();

        LibSrchResponseDto response = libraryService.searchLibraries(requestDto).getFirst();

        return responseTemplate.success(response, HttpStatus.OK);
    }
}
