package com.book.backend.domain.message.controller;

import com.book.backend.domain.message.dto.MessageRequestDto;
import com.book.backend.domain.message.dto.MessageResponseDto;
import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.message.service.MessageService;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name="채팅", description = "메세지 불러오기 / 메세지 저장")
public class MessageController {
    private final MessageService messageService;
    private final ResponseTemplate responseTemplate;
    private final SimpMessageSendingOperations sendingOperations;

    // 채팅 저장하기 (apic 으로 테스트)
    @MessageMapping("/message")
    public void chat(MessageRequestDto messageRequestDto) {
        RequestLogger.param(new String[]{"messageRequestDto"}, messageRequestDto);
        MessageResponseDto response = messageService.saveMessage(messageRequestDto);
        sendingOperations.convertAndSend("/sub/message/" + messageRequestDto.getOpentalkId(), response); // 수신자들에게 전송
    }


    // 채팅 불러오기
    @Operation(summary="메세지 불러오기 (특정 오픈톡)", description="오픈톡 ID 를 입력으로 받아 pageSize개 데이터를 반환합니다. (pageNo로 페이지네이션)",
            parameters = {@Parameter(name = "opentalkId", description = "오픈톡 DB ID"), @Parameter(name = "pageNo", description = "페이지 번호(0부터)"), @Parameter(name = "pageSize", description = "페이지 당 개수")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)),
                    description = MessageResponseDto.description)})
    @GetMapping("/api/message/get")
    public ResponseEntity<?> getChat(@RequestParam String opentalkId, int pageNo, int pageSize) {
        RequestLogger.param(new String[]{"opentalkId, pageNo, pageSize"}, opentalkId, pageNo, pageSize);

        Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Message> MessagePage = messageService.getMessage(opentalkId, pageRequest);
        List<MessageResponseDto> response = messageService.pageToDto(MessagePage);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // swagger docs 에 남기기 위한 용도
    @Operation(summary="메세지 저장 (채팅 stomp 통신)", description="APIC 테스터기를 이용해서 stomp 통신을 합니다.  \n" +
            "- Request URL: ws://52.79.187.133:8080/ws-stomp  \n"+
            "- Destination Queue: /pub/message  \n"+
            "- Subscription URL: /sub/message/{opentalkId}",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = MessageRequestDto.class))),
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)),
                    description = MessageResponseDto.description)})
    @PostMapping("")
    public void chatForSwagger(MessageRequestDto messageRequestDto) {
        return;
    }
}
