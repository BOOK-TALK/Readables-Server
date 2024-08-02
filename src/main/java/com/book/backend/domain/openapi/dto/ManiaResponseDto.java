package com.book.backend.domain.openapi.dto;

import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ManiaResponseDto {
    private HashMap<String, HashMap<String, String[]>> response;

    public HashMap<String, HashMap<String, String[]>> makeTemplate() {
        HashMap<String, HashMap<String, String[]>> response = new HashMap<>();
        String[] fields = {"bookname", "authors", "publisher", "publication_year", "isbn13", "bookImageURL"};
        HashMap<String, String[]> book = new HashMap<>();
        book.put("book", fields);
        response.put("docs", book);
        return response;
    }
}

