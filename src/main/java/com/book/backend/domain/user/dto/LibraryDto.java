package com.book.backend.domain.user.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class LibraryDto {
    private String code;
    private String name;
}
