package com.book.backend.domain.message.dto;

import lombok.*;

import java.util.Date;

@Getter
@Builder
public class MessageResponseDto {
    private String nickname; //작성자
    private String profileImageUrl;
    private String content;
    private Date createdAt;

    public static final String description = "nickname : 작성자 닉네임 | profileImageUrl : 작성자 프사 | content : 내용 | createdAt : 작성일시";
}
