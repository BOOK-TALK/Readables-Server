package com.book.backend.domain.openapi.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegionCode {
    RC1("11"),
    RC2("21"),
    RC3("22"),
    RC4("23"),
    RC5("24"),
    RC6("25"),
    RC7("26"),
    RC8("29"),
    RC9("31"),
    RC10("32"),
    RC11("33"),
    RC12("34"),
    RC13("35"),
    RC14("36"),
    RC15("37"),
    RC16("38"),
    RC17("39");

    public final String code;
}
