package com.blog.pessoal.acelera.maker.integration;

import com.blog.pessoal.acelera.maker.model.Usuario;
import com.blog.pessoal.acelera.maker.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class TokenServiceIntegrationTest {

    @Autowired
    private TokenService tokenService;

    @Test
    void testToken() {

        Usuario usuario = new Usuario(1L, "Ana Oliveira", "anaOliveira", "senha123", null);

        String token = tokenService.generateToken(usuario.getUsuario());
        String subject = tokenService.validateToken(token);
        String role = tokenService.extractRole(token);

        assertEquals("anaOliveira", subject);
        assertEquals("ROLE_USER", role);

    }
}
