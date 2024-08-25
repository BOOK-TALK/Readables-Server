package com.book.backend.domain.message.mapper;

import com.book.backend.domain.message.dto.MessageRequestDto;
import com.book.backend.domain.message.dto.MessageResponseDto;
import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.opentalk.entity.Opentalk;
import com.book.backend.domain.opentalk.repository.OpentalkRepository;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageMapper {
    private final ModelMapper mapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OpentalkRepository opentalkRepository;

    @Transactional
    public Message convertToMessage(MessageRequestDto dto) {
        log.trace("MessageMapper > convertToMessage()");
        User user = userService.loadLoggedinUser();
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        String content = dto.getContent();
        Opentalk opentalk = opentalkRepository.findById(dto.getOpentalkId()).orElseThrow();

        Message message = new Message();
        message.setUser(user); // 보낸 사람
        message.setOpentalk(opentalk);
        message.setContent(content);
        message.setCreatedAt(new Date());

        return message;
    }

    public MessageResponseDto convertToMessageResponseDto(Message message) {
        log.trace("MessageMapper > convertToMessageResponseDto()");
        User user = userRepository.findByLoginId(message.getUser().getLoginId()).orElseThrow();
        return MessageResponseDto.builder()
                .nickname(user.getNickname())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
