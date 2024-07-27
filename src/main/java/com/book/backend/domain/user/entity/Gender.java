package com.book.backend.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Gender {
    G0("NOT_SELECTED"),
    G1("MAN"),
    G2("WOMAN");

    public final String name;
}
