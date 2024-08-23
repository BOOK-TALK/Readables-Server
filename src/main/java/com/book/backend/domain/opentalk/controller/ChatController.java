package com.book.backend.domain.opentalk.controller;

import com.book.backend.domain.message.dto.MessageRequestDto;
import com.book.backend.domain.message.dto.MessageResponseDto;
import com.book.backend.domain.opentalk.service.OpentalkService;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final OpentalkService opentalkService;
    private final ResponseTemplate responseTemplate;

    // 채팅 저장하기
    @MessageMapping("/chat")
    public void chat(MessageRequestDto messageRequestDto) {
        RequestLogger.param(new String[]{"messageRequestDto"}, messageRequestDto);
        MessageResponseDto response = opentalkService.saveMessage(messageRequestDto);
    }
}
