package com.blog.pessoal.acelera.maker.service;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioDTO;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.IntegridadeVioladaException;
import com.blog.pessoal.acelera.maker.exception.UsuarioJaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Usuario;
import jakarta.transaction.RollbackException;

public interface UsuarioService {

    Resposta realizarCadastro(UsuarioDTO usuarioDTO) throws UsuarioJaExisteException;

    Resposta realizaAtualizacao(Long id, UsuarioUpdateDTO usuarioDTO) throws UsuarioJaExisteException;

    Resposta realizaDelete(Long id);

    boolean verificaSeUsuarioExiste(String usuario);

    Usuario buscaUsuario(Long id);

}
