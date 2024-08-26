package com.book.backend.domain.opentalk.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class OpentalkDto {
    private Long id;
    private String isbn13;
    private String bookName;
    private String bookImageURL;

    public static final String description = "id : 오픈톡 DB ID | isbn13 : 책 ISBN | bookName : 책 제목 | bookImageURL : 책 이미지 URL";
}
