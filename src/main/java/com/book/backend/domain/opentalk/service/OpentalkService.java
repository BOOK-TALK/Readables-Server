package com.book.backend.domain.opentalk.service;

import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpentalkService {
    private final UserRepository userRepository;

    /* 해당 user의 즐찾 opentalk list 반환*/
    public List<?> main(String loginId) {
        User user =  userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getOpenTalkIds();
    }
}
