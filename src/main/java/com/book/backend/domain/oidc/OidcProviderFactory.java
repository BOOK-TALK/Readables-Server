package com.book.backend.domain.oidc;

import com.book.backend.domain.oidc.apple.AppleAuthProvider;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import com.book.backend.domain.oidc.kakao.KakaoAuthProvider;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OidcProviderFactory {

    private final Map<Provider, OidcProvider> authProviderMap = new EnumMap<>(Provider.class);  // OIDC 구현체별로 적절한 구현체를 저장
    private final KakaoAuthProvider kakaoAuthProvider;
    private final AppleAuthProvider appleAuthProvider;

    @PostConstruct
    private void initialize() {
        authProviderMap.put(Provider.KAKAO, kakaoAuthProvider);
        authProviderMap.put(Provider.APPLE, appleAuthProvider);
    }

    // 주어진 Provider와 idToken을 사용하여 해당 제공자의 ID를 반환
    public String getProviderId(Provider provider, String idToken) {
        return getProvider(provider).getProviderId(idToken);
    }

    // 주어진 Provider에 대응하는 OidcProvider를 가져옴
    private OidcProvider getProvider(final Provider provider) {
        final OidcProvider oidcProvider = authProviderMap.get(provider);

        if (oidcProvider == null) {
            throw new CustomException(ErrorCode.WRONG_PROVIDER);
        }

        return oidcProvider;
    }
}