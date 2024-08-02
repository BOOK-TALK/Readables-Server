package com.book.backend.domain.openapi.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ManiaEnum {
    docs(null),
        book(docs),
            no(book),
            bookname(book),
            authors(book),
            publisher(book),
            publication_year(book),
            isbn13(book),
            additional_symbol(book),
            vol(book),
            class_no(book),
            class_nm(book),
            bookImageURL(book);

    ManiaEnum(ManiaEnum parentEnum) {}

    private String name;
    private ManiaEnum parent;
    private List<ManiaEnum> children = new ArrayList<>();

    public List<ManiaEnum> children() {
        return this.children;
    }
}
