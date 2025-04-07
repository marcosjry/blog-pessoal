package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemDTO;
import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemToResponse;
import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.PermissaoNaoAutorizada;
import com.blog.pessoal.acelera.maker.model.Postagem;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.service.PostagemService;
import com.blog.pessoal.acelera.maker.util.CapturaSubject;
import com.blog.pessoal.acelera.maker.util.FormataRespostaGenerics;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/postagens")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;

    @PostMapping
    public ResponseEntity<String> criaPostagem(@Valid @RequestBody PostagemDTO postagemDTO) {
        String usuario = CapturaSubject.captura();
        Resposta resposta = postagemService.criarPostagem(postagemDTO, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta.getMensagem());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizaPostagem(@PathVariable Long id,@Valid @RequestBody PostagemUpdateDTO postagemDTO) throws PermissaoNaoAutorizada {
        String usuario = CapturaSubject.captura();
        Resposta resposta = postagemService.atualizaPostagem(id,postagemDTO, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta.getMensagem());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removePostagem(@PathVariable Long id) throws PermissaoNaoAutorizada {
        String usuario = CapturaSubject.captura();
        Resposta resposta = postagemService.removerPostagem(id, usuario);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(resposta.getMensagem());
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<PostagemToResponse>> filtrarPostagens(
            @RequestParam(required = false) Long autor,
            @RequestParam(required = false) Long tema
    ) {
        List<Postagem> postagens = postagemService.buscaPorFiltro(autor, tema);
        return ResponseEntity.accepted().body(
                FormataRespostaGenerics.retornaListaFormatada(
                        postagens,
                        p -> new PostagemToResponse(
                                p.getId(),
                                p.getTexto(),
                                p.getTitulo(),
                                p.getUserId().getId(),
                                p.getTema().getId()
                        )
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<PostagemToResponse>> listarTodasPostagens() {
        List<Postagem> dto = postagemService.buscaTodasPostagens();
        return ResponseEntity.accepted().body(
                FormataRespostaGenerics.retornaListaFormatada(
                        dto,
                        p -> new PostagemToResponse(
                                p.getId(),
                                p.getTexto(),
                                p.getTitulo(),
                                p.getUserId().getId(),
                                p.getTema().getId()
                        )
                )
        );
    }
}
