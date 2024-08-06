package com.book.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_ISBN(HttpStatus.BAD_REQUEST, "400", "숫자로만 구성된 13자리 ISBN을 입력해주세요."),
    INVALID_SEARCH_DT_FORMAT(HttpStatus.BAD_REQUEST, "400", "올바르지 않은 검색일자입니다. (yyyy-mm-dd) 형식을 맞춰주세요."),
    INVALID_SEARCH_DT_DATE(HttpStatus.BAD_REQUEST, "400", "올바르지 않은 검색일자입니다. (어제까지 데이터 조회 가능)"),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "401", "인증 오류가 발생했습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당하는 사용자를 찾을 수 없습니다."),
    LOGIN_ID_DUPLICATED(HttpStatus.CONFLICT,"409", "사용자의 아이디가 중복됩니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
