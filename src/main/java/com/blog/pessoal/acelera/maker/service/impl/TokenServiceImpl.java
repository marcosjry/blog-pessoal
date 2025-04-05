package com.blog.pessoal.acelera.maker.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.blog.pessoal.acelera.maker.service.TokenService;
import com.blog.pessoal.acelera.maker.util.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private SecretUtil secret;

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
            return "";
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

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
