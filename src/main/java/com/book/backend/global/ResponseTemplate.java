package com.book.backend.global;

import java.util.HashMap;
import java.util.LinkedHashMap;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

        return new ResponseEntity<>(response, status);
    }
}
