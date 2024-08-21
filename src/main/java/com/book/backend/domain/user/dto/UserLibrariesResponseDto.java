package com.book.backend.domain.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLibrariesResponseDto {
    private List<LibraryDto> libraries;

    public static final String description =
            "libraries : 도서관 목록";
}
