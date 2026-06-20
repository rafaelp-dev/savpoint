package com.savpoint.savpoint.service.configurations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.savpoint.savpoint.entities.UserEntity;
import com.savpoint.savpoint.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("jwt.secret")
    private String jwtSecret;

    @Value("jwt.expiration")
    private Long jwtExpiration;

    public String generateToken (UserEntity userEntity) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

            String token = JWT.create()
                    .withIssuer("savpoint-api")
                    .withSubject(userEntity.getEmail())
                    .withExpiresAt(genExpirationDate().plusSeconds(jwtExpiration))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro durante a criação do token", exception);
        }
    }

    public String validateToken (String token) {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

            return JWT.require(algorithm)
                    .withIssuer("savpoint-api")
                    .build()
                    .verify(token)
                    .getSubject();
    }

    private Instant genExpirationDate() {
        return DateUtils.nowBrasilia().toInstant(ZoneOffset.of("-03:00"));
    }
}
