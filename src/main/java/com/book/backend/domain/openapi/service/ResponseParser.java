package com.book.backend.domain.openapi.service;

import com.book.backend.domain.openapi.dto.response.ManiaResponseDto;
import java.util.Arrays;
import java.util.LinkedList;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class ResponseParser {

    public LinkedList<ManiaResponseDto> mania(JSONObject jsonResponse) {
        JSONArray docs = (JSONArray) jsonResponse.get("docs");

        LinkedList<ManiaResponseDto> responseList = new LinkedList<>();
        for (int i = 0; i < docs.size(); i++) {
            JSONObject temp = (JSONObject) docs.get(i);
            JSONObject doc = (JSONObject) temp.get("book");

            responseList.add(ManiaResponseDto.builder()
                    .bookname(doc.getAsString("bookname"))
                    .authors(doc.getAsString("authors"))
                    .publisher(doc.getAsString("publisher"))
                    .publication_year(doc.getAsString("publication_year"))
                    .isbn13(doc.getAsString("isbn13"))
                    .additional_symbol(doc.getAsString("additional_symbol"))
                    .vol(doc.getAsString("vol"))
                    .class_no(doc.getAsString("class_no"))
                    .class_nm(doc.getAsString("class_nm"))
                    .bookImageURL(doc.getAsString("bookImageURL"))
                    .build());
        }
        return responseList;
    }
    // TODO : API 별로 파싱 메소드 추가
}

