package com.book.backend.domain.message.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class MessageImgRequsetDto {
    private MultipartFile image;
}
