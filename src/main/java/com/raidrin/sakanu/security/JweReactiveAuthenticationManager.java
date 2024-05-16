package com.raidrin.sakanu.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.*;

public class JweReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final String secretKey;

    public JweReactiveAuthenticationManager(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();

        try {
            // Parse the JWE string
            JWEObject jweObject = JWEObject.parse(token);

            // Decrypt with shared key
            jweObject.decrypt(new DirectDecrypter(secretKey.getBytes()));

            // Extract payload
            SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();

            // Here you can verify the signature, omitted for brevity

            // Create the final authentication token
            Jwt jwt = Jwt.withTokenValue(token)
                    .header("alg", (String) jweObject.getHeader().getCustomParam("alg"))
                    .claim("exp", signedJWT.getJWTClaimsSet().getExpirationTime().getTime())
                    .claim("iat", signedJWT.getJWTClaimsSet().getIssueTime().getTime())
                    .subject(signedJWT.getJWTClaimsSet().getSubject())
                    .build();

            return Mono.just(new JwtAuthenticationToken(jwt, Collections.emptyList()));

        } catch (ParseException | JOSEException e) {
            return Mono.error(new AuthenticationServiceException("Failed to decode JWT token", e));
        }
    }
}