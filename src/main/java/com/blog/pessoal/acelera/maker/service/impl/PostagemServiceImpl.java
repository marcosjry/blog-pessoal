package com.blog.pessoal.acelera.maker.service.impl;

import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemDTO;
import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.IntegridadeVioladaException;
import com.blog.pessoal.acelera.maker.model.Postagem;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Tema;
import com.blog.pessoal.acelera.maker.model.Usuario;
import com.blog.pessoal.acelera.maker.repository.PostagemRepository;
import com.blog.pessoal.acelera.maker.service.PostagemService;
import com.blog.pessoal.acelera.maker.service.TemaService;
import com.blog.pessoal.acelera.maker.service.UsuarioService;
import com.blog.pessoal.acelera.maker.specification.PostagemSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class PostagemServiceImpl implements PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private TemaService temaService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public Resposta criarPostagem(PostagemDTO postagemDTO, String usuario) {
        criaPostagem(postagemDTO, usuario);
        return new Resposta("Postagem criada com sucesso.", "success");
    }

    public void criaPostagem(PostagemDTO postagemDTO, String userName) {

        Tema tema = temaService.buscaTema(postagemDTO.temaId());
        Usuario usuario = usuarioService.buscaUsuario(userName);

        Postagem postagem = new Postagem();
        postagem.setData(Date.from(Instant.now()));
        postagem.setTexto(postagemDTO.texto());
        postagem.setTitulo(postagemDTO.titulo());
        postagem.setUserId(usuario);
        postagem.setTema(tema);

        postagemRepository.save(postagem);
        usuarioService.salveUsuario(usuario);
    }

    @Override
    public Resposta atualizaPostagem(Long id, PostagemUpdateDTO postagemUpdateDTO) {
        atualizaInfoPostagem(id, postagemUpdateDTO);
        return new Resposta("Postagem atualizada com sucesso.", "success");
    }

    @Transactional
    public void atualizaInfoPostagem(Long id, PostagemUpdateDTO postagemUpdateDTO) {
        Postagem postagem = buscaPostagem(id);
        Postagem postagemAlterada = verificaCampos(postagem, postagemUpdateDTO);
        postagemRepository.save(postagemAlterada);
    }

    @Override
    public Postagem buscaPostagem(Long id) {
        return postagemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Postagem não encontrada."));
    }

    @Override
    public List<Postagem> buscaPorFiltro(Long autorId, Long temaId) {
        return postagemRepository.findAll(PostagemSpecification.filtrar(autorId, temaId));
    }

    @Override
    public List<Postagem> buscaTodasPostagens() {
        return postagemRepository.findAll();
    }

    @Override
    public Resposta removerPostagem(Long id, String usuario) throws IntegridadeVioladaException {
        removePostagem(id, usuario);
        return new Resposta("Postagem removida com sucesso.", "success");
    }

    public void removePostagem(Long id, String usuario) throws IntegridadeVioladaException {
        Postagem postagem = buscaPostagem(id);

        Usuario usuarioEncontrado = usuarioService.buscaUsuario(usuario);
        if(!Objects.equals(usuarioEncontrado.getId(), postagem.getUserId().getId()))
            throw new IntegridadeVioladaException("Usuário não autorizado.");
        postagemRepository.deleteById(postagem.getId());
    }

    public Postagem verificaCampos(Postagem postagem, PostagemUpdateDTO campos) {
        if(campos.texto() != null)
            postagem.setTexto(campos.texto());
        if(campos.titulo() != null)
            postagem.setTitulo(campos.titulo());
        return postagem;
    }

}
