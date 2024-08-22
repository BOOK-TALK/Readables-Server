package com.book.backend.util.oidc.kakao;

import com.book.backend.util.JwtUtil;
import com.book.backend.util.oidc.OidcProvider;
import com.book.backend.util.oidc.record.OidcPublicKeyList;
import com.book.backend.util.oidc.PublicKeyProvider;
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
