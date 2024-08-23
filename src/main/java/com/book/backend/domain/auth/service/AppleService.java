package com.book.backend.domain.auth.service;

import com.book.backend.domain.auth.dto.LoginSuccessResponseDto;
import com.book.backend.domain.oidc.OidcProviderFactory;
import com.book.backend.domain.oidc.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AppleService {
    private final OidcProviderFactory oidcProviderFactory;

//    @Transactional
//    public LoginSuccessResponseDto appleLogin(String idToken) {
//        log.trace("AppleService > appleLogin()");
//
//        String providerId = oidcProviderFactory.getProviderId(Provider.APPLE, idToken);
//
//        // appleId로 유저 조회
//    }
}
