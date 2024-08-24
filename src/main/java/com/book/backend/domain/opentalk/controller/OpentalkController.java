package com.book.backend.domain.opentalk.controller;

import com.book.backend.domain.opentalk.dto.OpentalkDto;
import com.book.backend.domain.opentalk.dto.OpentalkJoinResponseDto;
import com.book.backend.domain.opentalk.dto.OpentalkResponseDto;
import com.book.backend.domain.opentalk.service.OpentalkService;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import java.util.List;

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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/opentalk")
@RequiredArgsConstructor
@Slf4j
@Tag(name="오픈톡", description = "즐찾 오픈톡 삭제 / 메인 / 오픈톡 참여하기 / 즐찾 오픈톡 추가")
public class OpentalkController {
    private final OpentalkService opentalkService;
    private final ResponseTemplate responseTemplate;

    // 오픈톡 메인 화면 (현재 핫한 오픈톡, 내가 즐찾한 오픈톡)
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
        return responseTemplate.success(response, HttpStatus.OK);
    }


    // [오픈톡 참여하기]
    @Operation(summary="오픈톡 참여하기", description="isbn, pageSize를 입력으로 받아 오픈톡 ID, 즐찾여부, 채팅 내역 반환",
            parameters = {@Parameter(name = "isbn", description = "책 ISBN"), @Parameter(name = "pageSize", description = "페이지 당 개수")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = OpentalkJoinResponseDto.class)),
                    description = OpentalkJoinResponseDto.description)})
    @PostMapping("/join")
    public ResponseEntity<?> joinOpentalk(@RequestParam String isbn, int pageSize) {
        RequestLogger.param(new String[]{"isbn"}, isbn);
        OpentalkJoinResponseDto response = opentalkService.joinOpentalk(isbn, pageSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 즐겨찾기 추가
    @Operation(summary="즐찾 오픈톡 추가", description="opentalkId 를 입력으로 받아, 사용자의 최종 즐찾 opentalkId List 반환",
            parameters = {@Parameter(name = "opentalkId", description = "오픈톡 DB ID")})
    @PostMapping("/favorite")
    public ResponseEntity<?> addFavoriteOpentalk(@RequestParam Long opentalkId) {
        RequestLogger.param(new String[]{"opentalkId"}, opentalkId);
        List<Long> response = opentalkService.addFavoriteOpentalk(opentalkId);
        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 즐겨찾기 삭제
    @Operation(summary="즐찾 오픈톡 삭제", description="opentalkId 를 입력으로 받아, 사용자의 최종 즐찾 opentalkId List 반환",
            parameters = {@Parameter(name = "opentalkId", description = "오픈톡 DB ID")})
    @DeleteMapping("/favorite")
    public ResponseEntity<?> deleteFavoriteOpentalk(@RequestParam Long opentalkId) {
        RequestLogger.param(new String[]{"opentalkId"}, opentalkId);
        List<Long> response = opentalkService.deleteFavoriteOpentalk(opentalkId);
        return responseTemplate.success(response, HttpStatus.OK);
    }
}
