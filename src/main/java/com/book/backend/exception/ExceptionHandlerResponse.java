package com.book.backend.exception;

import com.book.backend.global.ResponseTemplate;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerResponse {
    @Autowired
    ResponseTemplate responseTemplate;

    // 유효성 검사 실패 시 BAD_REQUEST 로 반환
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<LinkedHashMap<String, Object>> handleCustomException(CustomException e) {
        return responseTemplate.fail(e, HttpStatus.BAD_REQUEST);
    }

    // 다른 예외 발생 시 내부 서버 오류 반환
    @ExceptionHandler(Exception.class)
    public ResponseEntity<LinkedHashMap<String, Object>> handleException(Exception e) {
        return responseTemplate.fail(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
