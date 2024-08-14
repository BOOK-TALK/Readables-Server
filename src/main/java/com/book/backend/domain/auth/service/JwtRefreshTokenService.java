package com.book.backend.domain.auth.service;

import com.book.backend.domain.auth.dto.JwtTokenDto;
import com.book.backend.domain.auth.entity.RefreshToken;
import com.book.backend.domain.auth.repository.RefreshTokenRepository;
import com.book.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JwtService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    void updateRefreshToken(JwtTokenDto jwtTokenDto, User user) {
        // RefreshToken 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUser(user);

        // 있으면 업데이트, 없을 시 새로 생성
        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(jwtTokenDto.getRefreshToken()));
        } else {
            refreshTokenRepository.save(
                    RefreshToken.builder()
                            .user(user)
                            .refreshToken(jwtTokenDto.getRefreshToken())
                            .build()
            );
        }
    }

}
