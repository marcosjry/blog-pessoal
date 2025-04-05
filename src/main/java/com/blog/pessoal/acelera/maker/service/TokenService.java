package com.blog.pessoal.acelera.maker.service;

import org.springframework.security.core.Authentication;

public interface TokenService {

    String generateToken(String usuario);

    String validateToken(String token);

}
