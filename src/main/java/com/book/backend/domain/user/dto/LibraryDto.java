package com.book.backend.domain.user.dto;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class LibraryDto {
    @Pattern(regexp = "\\d{6}")
    private String code;
    private String name;
}
