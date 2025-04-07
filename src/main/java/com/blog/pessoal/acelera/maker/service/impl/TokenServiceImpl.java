package com.blog.pessoal.acelera.maker.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.blog.pessoal.acelera.maker.service.TokenService;
import com.blog.pessoal.acelera.maker.util.SecretUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    public TokenServiceImpl(@Lazy SecretUtil secret) {
        this.secret = secret;
    }

    private final SecretUtil secret;

    @Override
    public String validateToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret.getSecret());
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            System.err.println("Token verification failed: " + exception.getMessage());
            throw new JWTVerificationException("Autorização Negada.");
        }
    }

    @Override
    public String generateToken(String usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getSecret());
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuario)
                    .withClaim("role", "ROLE_USER")
                    .withExpiresAt(Date.from(genExpirationDate()))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar Token JWT", exception);
        }
    }

    @Override
    public String extractRole(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getSecret());
            var decodedJWT = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);
            return decodedJWT.getClaim("role").asString(); // Retorna "ROLE_SERVICE" ou outro papel
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error while extracting role from token", exception);
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
