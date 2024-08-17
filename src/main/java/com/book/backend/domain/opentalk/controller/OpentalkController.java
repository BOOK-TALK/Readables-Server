package com.book.backend.domain.opentalk.controller;

import com.book.backend.domain.message.dto.MessageRequestDto;
import com.book.backend.domain.message.dto.MessageResponseDto;
import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.opentalk.OpentalkResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/opentalk")
@RequiredArgsConstructor
@Slf4j
public class OpentalkController {
    private final OpentalkService opentalkService;

    // 오픈톡 메인 화면 (현재 핫한 오픈톡, 내가 즐찾한 오픈톡)
    @Operation(summary="오픈톡 메인 화면", description="현재 핫한 오픈톡 top 5의 ID List 와 사용자가 즐겨찾기한 오픈톡 ID List 를 반환합니다.",
            parameters = {@Parameter(name = "loginId", description = "아이디")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = OpentalkResponseDto.class)),
                    description = OpentalkResponseDto.description)})
    @GetMapping("/main")
    public ResponseEntity<?> main(@RequestParam String loginId) {
        RequestLogger.param(new String[]{"loginId"}, loginId);

        OpentalkResponseDto response = OpentalkResponseDto.builder()
                .hotOpentalkList(opentalkService.hotOpentalk())
                .favoriteOpentalkList(new LinkedList<>(opentalkService.favoriteOpentalk(loginId)))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 채팅 불러오기
    // opentalkId 를 입력받으면, message 테이블에서 해당 opentalkId 의 메시지를 createdAt 최신순으로 25개 조회해서 반환한다.
    @GetMapping("/chat")
    public ResponseEntity<?> getChat(@RequestParam String opentalkId, int pageNo) {
        RequestLogger.param(new String[]{"opentalkId, pageNo"}, opentalkId, pageNo);

        Pageable pageRequest = PageRequest.of(pageNo, 5, Sort.by("createdAt").descending());
        Page<Message> MessagePage = opentalkService.getOpentalkMessage(opentalkId, pageRequest);
        List<MessageResponseDto> response = opentalkService.pageToDto(MessagePage);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 채팅 저장하기
    @PostMapping("/chat")
    public ResponseEntity<?> saveChat(@RequestBody MessageRequestDto messageRequestDto) {
        RequestLogger.param(new String[]{"messageRequestDto"}, messageRequestDto);
        MessageResponseDto response = opentalkService.saveMessage(messageRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
