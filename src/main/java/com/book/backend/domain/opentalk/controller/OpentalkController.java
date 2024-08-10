package com.book.backend.domain.opentalk.controller;

import com.book.backend.domain.openapi.dto.request.RecommendListRequestDto;
import com.book.backend.domain.openapi.dto.response.RecommendListResponseDto;
import com.book.backend.domain.opentalk.OpentalkResponseDto;
import com.book.backend.domain.opentalk.service.OpentalkService;
import com.book.backend.global.log.RequestLogger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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

    @GetMapping("/main")
    public ResponseEntity<?> main(@RequestParam String loginId) throws Exception {
        RequestLogger.param(new String[]{"loginId"}, loginId);

        OpentalkResponseDto response = OpentalkResponseDto.builder()
                .hotOpentalkList(opentalkService.hotOpentalk()) // 현재 핫한 오픈톡 5개
                .favoriteOpentalkList(new LinkedList<>(opentalkService.favoriteOpentalk(loginId))) // 내가 즐찾한 오픈톡
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
