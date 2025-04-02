package com.blog.pessoal.acelera.maker.service;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioDTO;
import com.blog.pessoal.acelera.maker.model.Usuario;

public interface UsuarioService {
    Usuario cadastrarUsuario(UsuarioDTO usuarioDTO);
}
