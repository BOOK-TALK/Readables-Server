package com.book.backend.global;

import java.util.LinkedHashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ResponseTemplate {

    // 성공 response 템플릿
    public ResponseEntity<LinkedHashMap<String, Object>> success(Object message, HttpStatus status) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", status.value());
        response.put("data", message);

        return new ResponseEntity<>(response, status);
    }

    // 예외 response 템플릿
    public ResponseEntity<LinkedHashMap<String, Object>> fail(Exception e, HttpStatus status){
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", status.value());
        response.put("message", e.getMessage());

        final String red = "\033[1;31m";
        final String blue = "\033[1;34m";
        log.error("\n========### 예외 발생 ###======== \n" + red +"{}"+ blue + "\n============================", e.getMessage());

        return new ResponseEntity<>(response, status);
    }
}
