package com.example.caba.gateway.security;

import java.util.Objects;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class IssuerValidator implements OAuth2TokenValidator<Jwt> {

    private final String expectedIssuer;

    private static final OAuth2Error ERROR =
            new OAuth2Error("invalid_token", "El issuer del token no es válido", null);

    public IssuerValidator(String expectedIssuer) {
        this.expectedIssuer = expectedIssuer;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (Objects.equals(expectedIssuer, token.getIssuer() != null ? token.getIssuer().toString() : null)) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(ERROR);
    }
}

