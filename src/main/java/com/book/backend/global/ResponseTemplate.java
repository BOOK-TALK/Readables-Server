package com.book.backend.global;

import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResponseTemplate {

    // 성공 response 템플릿
    public ResponseEntity<HashMap<String, Object>> success(Object message, HttpStatus status) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("statusCode", status.value());
        response.put("message", message);

        return new ResponseEntity<>(response, status);
    }

    // 예외 response 템플릿
    public ResponseEntity<HashMap<String, Object>> fail(Exception e, HttpStatus status){
        HashMap<String, Object> response = new HashMap<>();
        response.put("statusCode", status.value());
        response.put("message", e.getMessage());

        return new ResponseEntity<>(response, status);
    }
}
