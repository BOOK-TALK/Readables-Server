package com.book.backend.domain.oidc.apple;

import com.book.backend.domain.oidc.record.OidcPublicKeyList;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AppleAuthClient {

    private final RestClient restClient;
    @Value("${apple.publicKeyUri}")
    private final String publicKeyUri;

    // 공개키 목록 반환
    public OidcPublicKeyList getPublicKeys() {
        return restClient.get()
                .uri(publicKeyUri)
                .retrieve()
                .body(OidcPublicKeyList.class);
    }
}
