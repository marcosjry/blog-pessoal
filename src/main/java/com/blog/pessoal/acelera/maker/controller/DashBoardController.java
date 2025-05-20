package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.DTO.postagem.PostStatsByUserDTO;
import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemToResponse;
import com.blog.pessoal.acelera.maker.DTO.postagem.PostsByDate;
import com.blog.pessoal.acelera.maker.model.Postagem;
import com.blog.pessoal.acelera.maker.repository.PostagemRepository;
import com.blog.pessoal.acelera.maker.util.FormataRespostaGenerics;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/stats")
public class DashBoardController {

    @Autowired
    private PostagemRepository postagemRepository;

    @GetMapping("posts-per-user")
    public List<PostStatsByUserDTO> getPostsPerUser() {
        return postagemRepository.countPostsPerUser();
    }

    @GetMapping("posts-by-date")
    public List<PostsByDate> getLastPostsByPage() {
        return postagemRepository.lastPostsByQuery(PageRequest.of(0, 5));
    }

    @GetMapping("total-posts")
    public ResponseEntity<Map<String, Long>> getTotalPostsNumber() {
        Long count = postagemRepository.countNumOfPosts();
        return ResponseEntity.ok(Map.of("total", count));
    }

    @GetMapping("last-posts")
    @Operation(summary = "Listar últimas 5 postagens", description = "Retorna as últimas 5 postagens do sistema.")
    public ResponseEntity<List<PostagemToResponse>> listarUltimasPostagens() {
        Page<Postagem> toDto = postagemRepository.findAllByOrderByDataDesc(PageRequest.of(0, 5));
        return ResponseEntity.accepted().body(
                FormataRespostaGenerics.retornaListaFormatada(
                        toDto.getContent(),
                        p -> new PostagemToResponse(
                                p.getId(),
                                p.getTexto(),
                                p.getTitulo(),
                                p.getUserId().getId(),
                                p.getUserId().getNome(),
                                p.getTema().getDescricao(),
                                p.getData()
                        )
                )
        );
    }
}
