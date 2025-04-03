package com.blog.pessoal.acelera.maker.service;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioDTO;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Usuario;

public interface UsuarioService {

    Resposta realizarCadastro(UsuarioDTO usuarioDTO);

    Resposta realizaAtualizacao(Integer id, UsuarioDTO usuarioDTO);

    Resposta realizaDelete(Integer id);

    boolean verificaSeUsuarioExiste(String usuario);

    Usuario buscaUsuario(Integer id);

}
