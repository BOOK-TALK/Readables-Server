package com.book.backend.domain.oidc.apple;

import com.book.backend.domain.oidc.OidcProvider;
import com.book.backend.domain.oidc.PublicKeyProvider;
import com.book.backend.domain.oidc.record.OidcPublicKeyList;
import com.book.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
@RequiredArgsConstructor
public class AppleAuthProvider implements OidcProvider {
    private final AppleAuthClient appleAuthClient;
    private final PublicKeyProvider publicKeyProvider;
    private final JwtUtil jwtUtil;

    @Override
    public String getProviderId(final String idToken) {
        final PublicKey publicKey = getPublicKey(idToken);

        return jwtUtil.getAllClaimsByPublicKey(idToken, publicKey).getSubject();
    }

    private PublicKey getPublicKey(String idToken) {
        final OidcPublicKeyList oidcPublicKeyList = appleAuthClient.getPublicKeys();
        return publicKeyProvider.generatePublicKey(parseHeaders(idToken), oidcPublicKeyList);
    }
}
