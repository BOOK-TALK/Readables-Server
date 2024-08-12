package com.book.backend.domain.message.dto;

import com.book.backend.domain.user.entity.User;
import lombok.*;

import java.util.Date;

@Getter
@Builder
public class MessageResopnseDto {
    private User user; //작성자
    private String content;
    private Date createdAt;
}
