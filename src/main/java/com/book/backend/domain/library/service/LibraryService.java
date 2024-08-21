package com.book.backend.domain.library.service;

import com.book.backend.domain.openapi.dto.request.LibSrchRequestDto;
import com.book.backend.domain.openapi.dto.response.LibSrchResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LibraryService {
    private final OpenAPI openAPI;
    private final ResponseParser responseParser;

    public LinkedList<LibSrchResponseDto> searchLibraries(LibSrchRequestDto requestDto) throws Exception {
        log.trace("LibraryService > searchLibraries()");

        String subUrl = "libSrch";
        JSONObject JsonResponse = openAPI.connect(subUrl, requestDto, new LibSrchResponseDto());

        return new LinkedList<>(responseParser.libSrch(JsonResponse));
    }
}
