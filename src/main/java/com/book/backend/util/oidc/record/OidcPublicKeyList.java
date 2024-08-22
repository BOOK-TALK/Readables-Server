package com.book.backend.util.oidc.record;

import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;

import java.util.List;

public record OidcPublicKeyList(
        List<OidcPublicKey> keys
) {

    // kid, alg로 공개 키 검색
    public OidcPublicKey getMatchedKey(final String kid, final String alg) {
        return keys.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findAny()
                .orElseThrow(() -> new CustomException(ErrorCode.EXTERNAL_SERVER_ERROR));
    }
}
