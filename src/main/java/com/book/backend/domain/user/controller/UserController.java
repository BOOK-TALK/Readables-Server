package com.book.backend.domain.user.controller;

import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.domain.user.dto.*;
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

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final ResponseTemplate responseTemplate;
    private final UserMapper userMapper;
    private final RequestValidate requestValidate;

    @Operation(summary = "유저 정보 조회", description = "유저 정보를 불러옵니다.",
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

    @Operation(summary = "저장된 도서관 조회", description = "유저가 저장한 도서관 목록을 불러옵니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserLibrariesResponseDto.class)),
                description = UserLibrariesResponseDto.description)})
    @GetMapping("/libraries")
    public ResponseEntity<?> getUserLibraries() {
        log.trace("UserController > getUserLibraries()");

        User user = userService.loadLoggedinUser();

        UserLibrariesResponseDto responseDto = new UserLibrariesResponseDto();
        responseDto.setLibraries(userService.getLibraries(user));

        return responseTemplate.success(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "저장된 도서관 수정", description = "저장된 도서관 목록을 수정합니다. 최대 3개의 도서관을 등록할 수 있습니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserLibrariesResponseDto.class)),
                    description = UserLibrariesResponseDto.description)})
    @PutMapping("/libraries/edit")
    public ResponseEntity<?> editUserLibraries(@RequestBody UserLibrariesRequestDto requestDto) {
        log.trace("UserController > editUserLibrary()");

        User user = userService.loadLoggedinUser();
        User updatedUser = userService.updateUserLibraries(user, requestDto);

        UserLibrariesResponseDto responseDto = new UserLibrariesResponseDto();
        responseDto.setLibraries(userService.getLibraries(updatedUser));

        return responseTemplate.success(responseDto, HttpStatus.OK);
    }

}
