package com.raidrin.sakanu.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
        public String getSubClaim(String token) {
            System.out.println("Token: " + token);
            // Decode the token
            DecodedJWT jwt = JWT.decode(token.replace("Bearer", "").trim());

            // Get the sub claim
            return jwt.getClaim("sub").asString();
        }
}