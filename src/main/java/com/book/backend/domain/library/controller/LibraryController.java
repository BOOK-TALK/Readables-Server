package com.book.backend.domain.library.controller;

import com.book.backend.domain.library.service.LibraryService;
import com.book.backend.domain.openapi.dto.request.LibSrchRequestDto;
import com.book.backend.domain.openapi.dto.response.LibSrchResponseDto;
import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.global.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.Objects;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
@Slf4j
public class LibraryController {
    private final RequestValidate requestValidate;
    private final LibraryService libraryService;
    private final ResponseTemplate responseTemplate;

    @GetMapping("/libraries/searchByRegion")
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
}
