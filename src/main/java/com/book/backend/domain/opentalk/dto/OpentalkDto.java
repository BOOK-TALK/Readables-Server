package com.book.backend.domain.opentalk.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class OpentalkDto {
    private Long id;
    private String bookName;
    private String bookImageURL;
}
