package com.blog.pessoal.acelera.maker.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioLoginDTO;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioLoginReqDTO;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioLoginResponseDTO;
import com.blog.pessoal.acelera.maker.exception.UsuarioSenhaInvalidoException;
import com.blog.pessoal.acelera.maker.model.Usuario;
import com.blog.pessoal.acelera.maker.service.TokenService;
import com.blog.pessoal.acelera.maker.service.UsuarioLoginService;
import com.blog.pessoal.acelera.maker.service.UsuarioService;
import com.blog.pessoal.acelera.maker.util.FormataRespostaGenerics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UsuarioLoginServiceImpl implements UsuarioLoginService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Override
    public UsuarioLoginResponseDTO realizaLogin(UsuarioLoginReqDTO usuarioLoginDTO) throws UsuarioSenhaInvalidoException {
        Usuario usuario = usuarioService.buscaUsuario(usuarioLoginDTO.usuario());

        boolean validaSenha = passwordEncoder.matches(usuarioLoginDTO.senha(), usuario.getSenha());
        if(!validaSenha)
            throw new UsuarioSenhaInvalidoException("Usuário ou senha Inválido.");

        String token = tokenService.generateToken(usuario.getUsuario());

        return FormataRespostaGenerics.retornaFormatado(usuario,
                user -> new UsuarioLoginResponseDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getUsuario(),
                        usuario.getFoto(),
                        token
                )
        );
    }

    @Override
    public Map<String, String> validaToken(String token) {
        return this.realizaValidacaoToken(token);
    }

    public Map<String, String> realizaValidacaoToken(String token) {
        try {
            String message = this.tokenService.validateToken(token);
            return Map.of("message", message);
        } catch (JWTVerificationException e) {
            return Map.of("message", e.getMessage());
        }
    }
}
