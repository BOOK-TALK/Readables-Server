package com.book.backend.domain.message.mapper;

import com.book.backend.domain.message.dto.MessageRequestDto;
import com.book.backend.domain.message.dto.MessageResponseDto;
import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.opentalk.entity.Opentalk;
import com.book.backend.domain.opentalk.repository.OpentalkRepository;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final ModelMapper mapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OpentalkRepository opentalkRepository;

    public Message convertToMessage(MessageRequestDto messageRequestDto) {
        Message message = mapper.map(messageRequestDto, Message.class);
        User user = userService.loadLoggedinUser();
        Opentalk opentalk = opentalkRepository.findById(messageRequestDto.getOpentalkId()).orElseThrow();

        message.setUser(user);
        message.setOpentalk(opentalk);
        message.setCreatedAt(new Date());

        return message;
    }
    public MessageResponseDto convertToMessageResponseDto(Message message) {
        User user = userRepository.findByLoginId(message.getUser().getLoginId()).orElseThrow();
        return MessageResponseDto.builder()
                .nickname(user.getNickname())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
