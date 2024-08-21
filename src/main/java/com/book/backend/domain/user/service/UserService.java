package com.book.backend.domain.user.service;

import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.domain.user.dto.LibraryDto;
import com.book.backend.domain.user.dto.UserInfoDto;
import com.book.backend.domain.user.dto.UserLibrariesRequestDto;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.mapper.UserMapper;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RequestValidate requestValidate;

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
        log.trace("UserService > updateUserInfo()");

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
    public User updateUserLibraries(User user, UserLibrariesRequestDto dto) {
        log.trace("UserService > updateUserLibraries()");

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 리스트 사이즈가 3보다 크면 오류 반환
        if (dto.getLibraries().size() > 3) {
            throw new CustomException(ErrorCode.LIST_SIZE_EXCEEDED);
        }

        user.getLibraries().clear();

        for (LibraryDto library : dto.getLibraries()) {
            if (library.getCode().isBlank() || library.getName().isBlank()) {
                continue;
            }

            requestValidate.isValidLibCode(library.getCode());
            user.getLibraries().add(library);
        }
        userRepository.save(user);

        return user;
    }

    public List<LibraryDto> getLibraries(User user) {
        log.trace("UserService > getLibraries()");

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return user.getLibraries();
    }
}
