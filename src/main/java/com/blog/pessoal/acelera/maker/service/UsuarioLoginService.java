package com.blog.pessoal.acelera.maker.service;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioLoginDTO;
import com.blog.pessoal.acelera.maker.exception.UsuarioSenhaInvalidoException;

public interface UsuarioLoginService {

    UsuarioLoginDTO realizaLogin(UsuarioLoginDTO usuarioLoginDTO) throws UsuarioSenhaInvalidoException;
}
