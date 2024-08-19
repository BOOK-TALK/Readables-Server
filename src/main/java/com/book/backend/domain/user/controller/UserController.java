package com.book.backend.domain.user.controller;

import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.domain.user.dto.UserInfoDto;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.mapper.UserMapper;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.global.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "유저 정보 불러오기", description = "유저 정보를 불러옵니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class)),
                    description = UserDto.description)})
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        log.trace("UserController > getUserInfo()");

        User user = userService.loadLoggedinUser();
        UserDto userDto = userMapper.convertToUserDto(user);

        return responseTemplate.success(userDto, HttpStatus.OK);
    }

    @Operation(summary = "유저 정보 수정", description = "유저의 변경 가능한 정보를 수정합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserInfoDto.class)),
                    description = UserInfoDto.description)})
    @PutMapping("/info/edit")
    public ResponseEntity<?> editUserInfo(@Valid @RequestBody UserInfoDto requestDto) {
        log.trace("UserController > editUserInfo()");

        User user = userService.loadLoggedinUser();
        User updatedUser = userService.updateUserInfo(user, requestDto);

        UserInfoDto userInfoDto = userMapper.convertToUserInfoDto(updatedUser);

        return responseTemplate.success(userInfoDto, HttpStatus.OK);
    }

}
