package com.blog.pessoal.acelera.maker.service;

public interface TokenService {

    String generateToken(String usuario);

    String validateToken(String token);

    String extractRole(String token);
}
