package com.blog.pessoal.acelera.maker.service.impl;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioLoginDTO;
import com.blog.pessoal.acelera.maker.exception.UsuarioSenhaInvalidoException;
import com.blog.pessoal.acelera.maker.model.Usuario;
import com.blog.pessoal.acelera.maker.service.TokenService;
import com.blog.pessoal.acelera.maker.service.UsuarioLoginService;
import com.blog.pessoal.acelera.maker.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public String realizaLogin(UsuarioLoginDTO usuarioLoginDTO) throws UsuarioSenhaInvalidoException {
        Usuario usuario = usuarioService.buscaUsuario(usuarioLoginDTO.usuario());
        boolean validaSenha = passwordEncoder.matches(usuarioLoginDTO.senha(), usuario.getSenha());
        if(!validaSenha)
            throw new UsuarioSenhaInvalidoException("Usuario ou senha Inválido.");

        return tokenService.generateToken(usuario.getUsuario());
    }
}
