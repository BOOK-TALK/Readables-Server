package com.book.backend.domain.message.service;

import com.book.backend.domain.auth.service.CustomUserDetailsService;
import com.book.backend.domain.message.dto.MessageRequestDto;
import com.book.backend.domain.message.dto.MessageResponseDto;
import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.message.mapper.MessageMapper;
import com.book.backend.domain.message.repository.MessageRepository;
import com.book.backend.domain.opentalk.entity.Opentalk;
import com.book.backend.domain.opentalk.repository.OpentalkRepository;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import com.book.backend.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final OpentalkRepository opentalkRepository;
    private final MessageMapper messageMapper;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    public MessageResponseDto saveMessage(MessageRequestDto messageRequestDto){
        log.trace("MessageService > saveMessage()");
        // 토큰 유효성 검사
        String token = messageRequestDto.getJwtToken();
        validateToken(token);

        // message DB에 저장
        Message message = messageMapper.convertToMessage(messageRequestDto);
        try{
            messageRepository.save(message);
        } catch (Exception e){
            throw new CustomException(ErrorCode.MESSAGE_SAVE_FAILED);
        }
        return messageMapper.convertToMessageResponseDto(message);
    }


    public void validateToken(String token) {
        try{
            String username = jwtUtil.getUsernameFromToken(token);  // username 가져옴
            // 현재 SecurityContextHolder에 인증객체가 있는지 확인
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails;
                try {
                    userDetails = userDetailsService.loadUserByUsername(username);
                } catch (CustomException e1) {
                    try {
                        userDetails = userDetailsService.loadUserByKakaoId(username);
                    } catch (CustomException e2) {
                        userDetails = userDetailsService.loadUserByAppleId(username);
                    }
                }

                // 토큰 유효성 검증
                if (jwtUtil.isValidToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticated
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticated);
                }
            }
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.JWT_EXPIRED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.WRONG_JWT_TOKEN);
        }
    }

    public Page<Message> getMessage(String opentalkId, Pageable pageRequest){
        log.trace("MessageService > getOpentalkMessage()");
        // 오픈톡 ID로 opentlak 객체 찾기
        Opentalk opentalk = opentalkRepository.findByOpentalkId(Long.parseLong(opentalkId)).orElseThrow(() -> new CustomException(ErrorCode.OPENTALK_NOT_FOUND));
        return messageRepository.findAllByOpentalk(opentalk, pageRequest);
    }

    public List<MessageResponseDto> pageToDto(Page<Message> page){
        log.trace("MessageService > pageToDto()");
        List<Message> messages = page.getContent();
        List<MessageResponseDto> messageList = new LinkedList<>();

        for(Message message : messages){
            messageList.add(messageMapper.convertToMessageResponseDto(message));
        }
        return messageList;
    }
}
