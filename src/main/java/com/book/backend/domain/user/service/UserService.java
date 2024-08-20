package com.book.backend.domain.user.service;

import com.book.backend.domain.user.dto.UserInfoDto;
import com.book.backend.domain.user.dto.UserLibrariesDto;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.mapper.UserMapper;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User loadLoggedinUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return findByUsername(username);
    }

    public User findByUsername(String username) {
        try {
            return findByLoginId(username);
        } catch (IllegalArgumentException e) {
            return findByKakaoId(username);
        }
    }

    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public User findByKakaoId(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public User updateUserInfo(User user, UserInfoDto requestDto) {
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        user.setNickname(requestDto.getNickname());
        user.setGender(userMapper.convertStringToGender(requestDto.getGender()));
        user.setBirthDate(requestDto.getBirthDate());
        userRepository.save(user);

        return user;
    }

    @Transactional
    public User updateUserLibraries(User user, UserLibrariesDto dto) {
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        user.getLibraries().clear();
        user.getLibraries().add(dto.getLibCode1());
        user.getLibraries().add(dto.getLibCode2());
        user.getLibraries().add(dto.getLibCode3());
        userRepository.save(user);

        return user;
    }

    public List<String> getLibraries(User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return user.getLibraries();
    }
}
