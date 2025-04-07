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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/postagens")
@Tag(name = "Postagens", description = "Endpoints relacionados às postagens")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;

    @PostMapping
    @Operation(summary = "Criar nova postagem", description = "Cria uma nova postagem vinculada ao usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Postagem criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    public ResponseEntity<String> criaPostagem(@Valid @RequestBody PostagemDTO postagemDTO) {
        String usuario = CapturaSubject.captura();
        Resposta resposta = postagemService.criarPostagem(postagemDTO, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta.getMensagem());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar postagem", description = "Atualiza uma postagem existente, se for do usuário autenticado.")
    public ResponseEntity<String> atualizaPostagem(@Parameter(description = "ID da postagem") @PathVariable Long id, @Valid @RequestBody PostagemUpdateDTO postagemDTO) throws PermissaoNaoAutorizada {
        String usuario = CapturaSubject.captura();
        Resposta resposta = postagemService.atualizaPostagem(id,postagemDTO, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta.getMensagem());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover postagem", description = "Remove uma postagem pertencente ao usuário autenticado.")
    public ResponseEntity<String> removePostagem(@Parameter(description = "ID da postagem") @PathVariable Long id) throws PermissaoNaoAutorizada {
        String usuario = CapturaSubject.captura();
        Resposta resposta = postagemService.removerPostagem(id, usuario);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(resposta.getMensagem());
    }
    @Operation(
            summary = "Filtrar postagens",
            description = "Filtra postagens por autor e/ou tema.\n" +
                    "O filtro é feito passando o autor (usuario_id) ou tema (tema_id)\n" +
                    "exemplo: \n"+
                    "http://localhost:8080/api/filtro?autor=1&tema=10\n" +
                    "                   ou\n" +
                    "http://localhost:8080/api/filtro?autor=1",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/filtro")
    public ResponseEntity<List<PostagemToResponse>> filtrarPostagens(
            @Parameter(description = "ID do autor") @RequestParam(required = false) Long autor,
            @Parameter(description = "ID do tema") @RequestParam(required = false) Long tema
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
    @Operation(summary = "Listar postagens", description = "Retorna todas as postagens do sistema.")
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
