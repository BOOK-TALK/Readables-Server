package com.book.backend.domain.opentalk.controller;

import com.book.backend.domain.opentalk.dto.OpentalkDto;
import com.book.backend.domain.opentalk.dto.OpentalkJoinResponseDto;
import com.book.backend.domain.opentalk.dto.OpentalkResponseDto;
import com.book.backend.domain.message.dto.MessageRequestDto;
import com.book.backend.domain.message.dto.MessageResponseDto;
import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.opentalk.service.OpentalkService;
import com.book.backend.domain.user.entity.User;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/opentalk")
@RequiredArgsConstructor
@Slf4j
@Tag(name="오픈톡", description = "메인 / 채팅 불러오기 / 오픈톡 참여하기")
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

    // 채팅 불러오기
    @Operation(summary="특정 오픈톡 채팅 불러오기", description="오픈톡 ID 를 입력으로 받아 pageSize개 데이터를 반환합니다. (pageNo로 페이지네이션)",
            parameters = {@Parameter(name = "opentalkId", description = "오픈톡 DB ID"), @Parameter(name = "pageNo", description = "페이지 번호(0부터)"), @Parameter(name = "pageSize", description = "페이지 당 개수")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)),
                    description = MessageResponseDto.description)})
    @GetMapping("/chat/get")
    public ResponseEntity<?> getChat(@RequestParam String opentalkId, int pageNo, int pageSize) {
        RequestLogger.param(new String[]{"opentalkId, pageNo, pageSize"}, opentalkId, pageNo, pageSize);

        Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Message> MessagePage = opentalkService.getOpentalkMessage(opentalkId, pageRequest);
        List<MessageResponseDto> response = opentalkService.pageToDto(MessagePage);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 채팅 저장하기
    @MessageMapping("/chat/{opentalkId}")
    @SendTo("/sub/chat/{opentalkId}")
    public ResponseEntity<?> chat(@DestinationVariable Long opentalkId, MessageRequestDto messageRequestDto) {
        RequestLogger.param(new String[]{"opentalkId, messageRequestDto"}, opentalkId, messageRequestDto);
        MessageResponseDto response = opentalkService.saveMessage(opentalkId, messageRequestDto);
        return responseTemplate.success(response, HttpStatus.OK); // pub, sub 모두 이걸 받나?
    }

    // [오픈톡 참여하기]
    @Operation(summary="오픈톡 참여하기", description="isbn, pageSize를 입력으로 받아, 오픈톡 ID, 채팅 내역 반환",
            parameters = {@Parameter(name = "isbn", description = "책 ISBN"), @Parameter(name = "pageSize", description = "페이지 당 개수")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = OpentalkResponseDto.class)),
                    description = OpentalkResponseDto.description)})
    @PostMapping("/join")
    public ResponseEntity<?> joinOpentalk(@RequestParam String isbn, int pageSize) {
        RequestLogger.param(new String[]{"isbn"}, isbn);
        OpentalkJoinResponseDto response = opentalkService.joinOpentalk(isbn, pageSize);

        return responseTemplate.success(response, HttpStatus.OK);
    }
}
