package com.book.backend.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Sex {
    S0("NOT_SELECTED"),
    S1("MAN"),
    S2("WOMAN");

    public final String name;
}
