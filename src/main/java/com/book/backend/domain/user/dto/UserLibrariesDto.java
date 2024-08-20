package com.book.backend.domain.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLibrariesDto {
    private String libCode1;
    private String libCode2;
    private String libCode3;
}
