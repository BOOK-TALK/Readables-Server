package com.book.backend.domain.openapi.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgeRangeCode {
    AR0("0"),
    AR6("6"),
    AR8("8"),
    AR14("14"),
    AR20("20"),
    AR30("30"),
    AR40("40"),
    AR50("50"),
    AR60("60");

    public final String code;
}
