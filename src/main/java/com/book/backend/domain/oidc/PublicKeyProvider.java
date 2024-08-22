package com.book.backend.domain.oidc;

import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import com.book.backend.domain.oidc.record.OidcPublicKey;
import com.book.backend.domain.oidc.record.OidcPublicKeyList;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
public class PublicKeyProvider {
    public PublicKey generatePublicKey(final Map<String, String> tokenHeaders, final OidcPublicKeyList publicKeys) {
        final OidcPublicKey publicKey = publicKeys.getMatchedKey(tokenHeaders.get("kid"), tokenHeaders.get("alg"));

        return getPublicKey(publicKey);
    }

    // OidcPublicKey 객체에서 n, e 추출 -> 바이트 배열로 디코딩
    private PublicKey getPublicKey(final OidcPublicKey publicKey) {
        // n, e 추출
        final byte[] nBytes = Base64.getUrlDecoder().decode(publicKey.n());
        final byte[] eBytes = Base64.getUrlDecoder().decode(publicKey.e());

        // 바이트 배열로 디코딩
        final RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes),
                                                                    new BigInteger(1, eBytes));

        try {
            return KeyFactory.getInstance(publicKey.kty()).generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CustomException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }
    }
}
