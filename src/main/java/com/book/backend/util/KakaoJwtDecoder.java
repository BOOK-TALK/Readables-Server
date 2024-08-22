package com.book.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoJwtDecoder {
    private PublicKey publicKey;

    public PublicKey getPublicKey(String base64PublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public KakaoJwtDecoder(String base64PublicKey) {
        try {
            this.publicKey = getPublicKey(base64PublicKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Error initializing public key", e);
        }
    }

    public Claims getAllClaims(String token) {
        log.info("getAllClaims token = {}", token);
        return Jwts.parser()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getAud(Claims body) {
        return String.valueOf(body.get("aud"));
    }

    public String getSub(Claims body) {
        return String.valueOf(body.get("sub"));
    }

    public String getAuthTime(Claims body) {
        return String.valueOf(body.get("auth_time"));
    }

    public String getNickname(Claims body) {
        return String.valueOf(body.get("nickname"));
    }

    public String getPicture(Claims body) {
        return String.valueOf(body.get("picture"));
    }
}
