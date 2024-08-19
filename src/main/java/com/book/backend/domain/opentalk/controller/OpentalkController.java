package com.book.backend.domain.opentalk.controller;

import com.book.backend.domain.opentalk.dto.OpentalkDto;
import com.book.backend.domain.opentalk.dto.OpentalkResponseDto;
import com.book.backend.domain.opentalk.service.OpentalkService;
import com.book.backend.global.log.RequestLogger;
import java.util.LinkedList;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/opentalk")
@RequiredArgsConstructor
@Slf4j
public class OpentalkController {
    private final OpentalkService opentalkService;

    @Operation(summary="오픈톡 메인 화면", description="현재 핫한 오픈톡 top 5의 ID List 와 사용자가 즐겨찾기한 오픈톡 ID List 를 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = OpentalkResponseDto.class)),
                    description = OpentalkResponseDto.description)})
    @GetMapping("/main")
    public ResponseEntity<?> opentalkMain() throws Exception {
        // 현재 핫한 오픈톡
        List<Long> hotOpentalkIdList = opentalkService.hotOpentalk();
        List<OpentalkDto> hotOpentalkList = opentalkService.getBookInfo(hotOpentalkIdList);

        // 내가 즐찾한 오픈톡
        List<Long> favoriteOpentalkIdList = opentalkService.favoriteOpentalk();
        List<OpentalkDto> favoriteOpentalkList = opentalkService.getBookInfo(favoriteOpentalkIdList);

        OpentalkResponseDto response = OpentalkResponseDto.builder()
                .hotOpentalkList(hotOpentalkList)
                .favoriteOpentalkList(favoriteOpentalkList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
