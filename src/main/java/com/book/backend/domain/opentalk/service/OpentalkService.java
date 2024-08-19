package com.book.backend.domain.opentalk.service;

import com.book.backend.domain.book.repository.BookRepository;
import com.book.backend.domain.detail.service.DetailService;
import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.message.repository.MessageRepository;
import com.book.backend.domain.openapi.dto.request.DetailRequestDto;
import com.book.backend.domain.openapi.dto.response.DetailResponseDto;
import com.book.backend.domain.openapi.service.ResponseParser;
import com.book.backend.domain.opentalk.dto.OpentalkDto;
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
import org.springdoc.core.service.OpenAPIService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpentalkService {
    private final UserRepository userRepository;
    private final UserOpentalkRepository userOpentalkRepository;
    private final MessageRepository messageRepository;
    private final OpentalkRepository opentalkRepository;
    private final BookRepository bookRepository;
    private final DetailService detailService;
    private final OpentalkResponseParser opentalkResponseParser;

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

    // 해당 오픈톡 id 와 맵핑되는 책 isbn 을 찾아서 8번 open API 로 title, imageUrl 불러오기
    public List<OpentalkDto> getBookInfo(List<Long> opentalkId) throws Exception {
        List<OpentalkDto> opentalkDtoList = new LinkedList<>();

        for(Long id : opentalkId) {
            OpentalkDto opentalkDto = OpentalkDto.builder().build();
            String isbn = bookRepository.findIsbnByBookId(opentalkRepository.findByOpentalkId(id));
            DetailResponseDto detailResponseDto = detailService.detail(DetailRequestDto.builder().isbn13(isbn).build()); // 정보 가져오기
            opentalkResponseParser.setSimpleBookInfo(opentalkDto, detailResponseDto); // detailResponseDto 에서 title 이랑 imageUrl 만 추출하기
            opentalkDtoList.add(opentalkDto);
        }
        return opentalkDtoList;
    }
}
