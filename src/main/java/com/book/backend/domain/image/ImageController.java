package com.book.backend.domain.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImageController {
    // 이미지 파일을 request 로 받는다

    @PostMapping("/la")
    public ResponseEntity<String> la() {
        return ResponseEntity.ok("la");
    }

    @PostMapping(value = "/image", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        // Handle file upload logic here
        return ResponseEntity.ok("File uploaded successfully");
    }
}
