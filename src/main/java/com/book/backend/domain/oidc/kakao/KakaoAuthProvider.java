package com.book.backend.domain.oidc.kakao;

import com.book.backend.domain.oidc.OidcProvider;
import com.book.backend.domain.oidc.PublicKeyProvider;
import com.book.backend.domain.oidc.record.OidcPublicKeyList;
import com.book.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
@RequiredArgsConstructor
public class KakaoAuthProvider implements OidcProvider {
    private final KakaoAuthClient kakaoAuthClient;
    private final PublicKeyProvider publicKeyProvider;
    private final JwtUtil jwtUtil;

    @Override
    public String getProviderId(final String idToken) {
        final OidcPublicKeyList oidcPublicKeyList = kakaoAuthClient.getPublicKeys();
        final PublicKey publicKey = publicKeyProvider.generatePublicKey(parseHeaders(idToken), oidcPublicKeyList);

        return jwtUtil.getAllClaimsByPublicKey(idToken, publicKey).getSubject();
    }
}
