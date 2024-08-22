package com.book.backend.util.oidc;

import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

import static org.apache.tomcat.util.codec.binary.Base64.decodeBase64;

public interface OidcProvider {
    String getProviderId(String idToken);

    // 헤더 파싱
    default Map<String, String> parseHeaders(String token) {
        String header = token.split("\\.")[0];

        try {
            return new ObjectMapper().readValue(decodeBase64(header), Map.class);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.HEADER_PARSING_ERROR);
        }
    }
}
