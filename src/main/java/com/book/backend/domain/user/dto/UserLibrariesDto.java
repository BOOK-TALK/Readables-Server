package com.book.backend.domain.user.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLibrariesDto {
    private List<String> libCodeList;
}
