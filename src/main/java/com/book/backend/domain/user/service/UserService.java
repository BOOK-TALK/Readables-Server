package com.book.backend.domain.user.service;

import com.book.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    // TODO: 기능에 따른 회원 서비스 메서드 추가 예정
}
