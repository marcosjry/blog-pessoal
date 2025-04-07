package com.blog.pessoal.acelera.maker.service;

import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemDTO;
import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.PermissaoNaoAutorizada;
import com.blog.pessoal.acelera.maker.model.Postagem;
import com.blog.pessoal.acelera.maker.model.Resposta;

import java.util.List;

public interface PostagemService {

    Resposta criarPostagem(PostagemDTO postagemDTO, String username);

    Resposta atualizaPostagem(Long id, PostagemUpdateDTO postagemUpdateDTO, String usuario) throws PermissaoNaoAutorizada;

    Resposta removerPostagem(Long id, String usuario) throws PermissaoNaoAutorizada;

    Postagem buscaPostagem(Long id);

    List<Postagem> buscaPorFiltro(Long autorId, Long temaId);

    List<Postagem> buscaTodasPostagens();
}
