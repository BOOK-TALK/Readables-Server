package com.book.backend.domain.opentalk.service;

import com.book.backend.domain.message.dto.MessageRequestDto;
import com.book.backend.domain.message.dto.MessageResponseDto;
import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.message.mapper.MessageMapper;
import com.book.backend.domain.message.repository.MessageRepository;
import com.book.backend.domain.opentalk.entity.Opentalk;
import com.book.backend.domain.opentalk.repository.OpentalkRepository;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.domain.userOpentalk.entity.UserOpentalk;
import com.book.backend.domain.userOpentalk.repository.UserOpentalkRepository;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpentalkService {
    private final UserRepository userRepository;
    private final UserOpentalkRepository userOpentalkRepository;
    private final MessageRepository messageRepository;
    private final OpentalkRepository opentalkRepository;
    private final MessageMapper messageMapper;

    /* message 테이블에서 최근 200개 데이터 조회 -> opentalkId 기준으로 count 해서 가장 빈번하게 나오는 top 5 id 반환*/
    public List<Long> hotOpentalk() {
        List<Message> recent200Messages = messageRepository.findTop200ByOrderByCreatedAtDesc();

        // (key : opentalk_id, value : 출현빈도)
        Map<Long, Long> opentalkIdCountMap = recent200Messages.stream().collect(
                Collectors.groupingBy(message -> message.getOpentalk().getOpentalkId(), Collectors.counting())
        );
        // value 순으로 정렬해서 top 5 id 반환
        return opentalkIdCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    /* 해당 user의 즐찾 opentalk list 반환*/
    public List<Long> favoriteOpentalk(String loginId) {
        User user =  userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<UserOpentalk> opentalkList = userOpentalkRepository.findAllByUserId(user);

        List<Long> opentalkIds = new LinkedList<>();
        for(UserOpentalk userOpentalk : opentalkList) {
            opentalkIds.add(userOpentalk.getOpentalkId().getOpentalkId());
        }
        return opentalkIds;
    }

    public Page<Message> getOpentalkMessage(String opentalkId, Pageable pageRequest){
        // 오픈톡 ID로 opentlak 객체 찾기
        Opentalk opentalk = opentalkRepository.findByOpentalkId(Long.parseLong(opentalkId)).orElseThrow(() -> new CustomException(ErrorCode.OPENTALK_NOT_FOUND));
        return messageRepository.findAllByOpentalk(opentalk, pageRequest);
    }

    public List<MessageResponseDto> pageToDto(Page<Message> page){
        List<Message> messages = page.getContent();
        List<MessageResponseDto> messageList = new LinkedList<>();

        for(Message message : messages){
            messageList.add(messageMapper.convertToMessageResponseDto(message));
        }
        return messageList;
    }

    // message 저장
    public MessageResponseDto saveMessage(MessageRequestDto messageRequestDto){
        Message message = messageMapper.convertToMessage(messageRequestDto);
        try{
            messageRepository.save(message);
        } catch (Exception e){
            throw new CustomException(ErrorCode.MESSAGE_SAVE_FAILED);
        }
        return messageMapper.convertToMessageResponseDto(message);
    }
}
