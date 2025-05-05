package com.blog.pessoal.acelera.maker.service;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioLoginReqDTO;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioLoginResponseDTO;
import com.blog.pessoal.acelera.maker.exception.UsuarioSenhaInvalidoException;

import java.util.Map;

public interface UsuarioLoginService {

    UsuarioLoginResponseDTO realizaLogin(UsuarioLoginReqDTO usuarioLoginDTO) throws UsuarioSenhaInvalidoException;

    Map<String, String> validaToken(String token);
}
