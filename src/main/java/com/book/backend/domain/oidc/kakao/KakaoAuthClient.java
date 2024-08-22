package com.book.backend.domain.oidc.kakao;

import com.book.backend.domain.oidc.record.OidcPublicKeyList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoAuthClient {

    private final RestClient restClient;

    private final String publicKeyUri;

    public KakaoAuthClient(
            RestClient restClient,
            @Value("${kakao.publicKeyUri}") String publicKeyUri
    ) {
        this.restClient = restClient;
        this.publicKeyUri = publicKeyUri;
    }

    // 공개키 목록 반환
    public OidcPublicKeyList getPublicKeys() {
        return restClient.get()
                .uri(publicKeyUri)
                .retrieve()
                .body(OidcPublicKeyList.class);
    }
}