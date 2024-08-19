package com.book.backend.domain.user.controller;

import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.domain.user.dto.UserInfoDto;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.mapper.UserMapper;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.global.ResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final ResponseTemplate responseTemplate;
    private final UserMapper userMapper;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        log.trace("UserController > getUserInfo()");

        User user = userService.loadLoggedinUser();
        UserDto userDto = userMapper.convertToUserDto(user);

        return responseTemplate.success(userDto, HttpStatus.OK);
    }

    @PutMapping("/info/edit")
    public ResponseEntity<?> editUserInfo(@Valid @RequestBody UserInfoDto requestDto) {
        log.trace("UserController > editUserInfo()");

        User user = userService.loadLoggedinUser();
        User updatedUser = userService.updateUserInfo(user, requestDto);

        UserInfoDto userInfoDto = userMapper.convertToUserInfoDto(updatedUser);

        return responseTemplate.success(userInfoDto, HttpStatus.OK);
    }

}
