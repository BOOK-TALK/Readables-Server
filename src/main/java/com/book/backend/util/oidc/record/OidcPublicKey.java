package com.book.backend.util.oidc.record;

public record OidcPublicKey(
        String kid,
        String kty,
        String alg,
        String use,
        String n,
        String e
) {
}
