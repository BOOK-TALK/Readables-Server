package com.book.backend.domain.opentalk.controller;

import com.book.backend.domain.opentalk.dto.OpentalkDto;
import com.book.backend.domain.opentalk.dto.OpentalkJoinResponseDto;
import com.book.backend.domain.opentalk.entity.Opentalk;
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

    // 현재 핫한 오픈톡
    @Operation(summary="현재 핫한 오픈톡", description="현재 핫한 오픈톡 top 3의 ID List를 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = OpentalkDto.class)),
                    description = OpentalkDto.description)})
    @GetMapping("/hot")
    public ResponseEntity<?> getHotOpentalk() throws Exception {
        // 현재 핫한 오픈톡
        List<Long> idList = opentalkService.getHotOpentalkIds();
        List<OpentalkDto> response = opentalkService.getBookInfo(idList);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // [오픈톡 참여하기]
    @Operation(summary="오픈톡 참여하기", description="isbn에 해당하는 오픈톡 DB ID, 즐찾여부 반환 & pageSize 만큼의 채팅 내역 반환",
            parameters = {@Parameter(name = "isbn", description = "책 ISBN"), @Parameter(name = "bookname", description = "책 이름"), @Parameter(name = "bookImageURL", description = "책 이미지 url"), @Parameter(name = "pageSize", description = "채팅 개수")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = OpentalkJoinResponseDto.class)),
                    description = OpentalkJoinResponseDto.description)})
    @PostMapping("/join")
    public ResponseEntity<?> joinOpentalk(@RequestParam String isbn, String bookname, String bookImageURL, int pageSize) {
        RequestLogger.param(new String[]{"isbn", "bookname", "bookImageURL",  "pageSize"}, isbn, bookname, bookImageURL, pageSize);
        OpentalkJoinResponseDto response = opentalkService.joinOpentalk(isbn, bookname, bookImageURL, pageSize);
        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 즐겨찾기 조회
    @Operation(summary="즐찾 오픈톡 조회", description="사용자가 즐겨찾기한 오픈톡 ID List 를 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = OpentalkDto.class)),
                    description = OpentalkDto.description)})
    @GetMapping("/favorite")
    public ResponseEntity<?> getFavoriteOpentalk() throws Exception {
        List<Long> idList = opentalkService.getFavoriteOpentalkIds();
        List<OpentalkDto> response = opentalkService.getBookInfo(idList);
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
