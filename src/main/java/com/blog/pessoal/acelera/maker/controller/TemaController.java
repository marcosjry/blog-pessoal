package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.DTO.tema.TemaDTO;
import com.blog.pessoal.acelera.maker.DTO.tema.TemaDTOResponse;
import com.blog.pessoal.acelera.maker.exception.PermissaoNaoAutorizada;
import com.blog.pessoal.acelera.maker.exception.TemaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Tema;
import com.blog.pessoal.acelera.maker.service.TemaService;
import com.blog.pessoal.acelera.maker.util.FormataRespostaGenerics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Temas", description = "Endpoints relacionados à criação, listagem, atualização e exclusão de temas.")
@RequestMapping(path = "/api/temas")
public class TemaController {

    @Autowired
    private TemaService temaService;

    @Operation(
            summary = "Cria tema",
            description = "Cria um novo tema",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> criarPostagem(@Valid @RequestBody TemaDTO temaDTO)
            throws TemaExisteException, RuntimeException {
        Resposta resposta = temaService.criarTema(temaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", resposta.getMensagem()));
    }

    @Operation(
            summary = "Atualiza um tema existente",
            description = "Atualiza os dados de um tema com base no ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tema atualizado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class, example = "Tema atualizado com sucesso")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tema não encontrado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Object.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Object.class)
                    )
            )
    })
    @PutMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> atualizaTema(
            @PathVariable Long id,
            @Valid @RequestBody TemaDTO temaDTO
    ) throws TemaExisteException, PermissaoNaoAutorizada {
        Resposta resposta = temaService.atualiza(id, temaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", resposta.getMensagem()));
    }

    /**
     * Remove um tema com base no ID
     * @param id Identificador do tema a ser removido
     * @return Mensagem de sucesso
     */
    @Operation(
            summary = "Remove um tema",
            description = "Remove um tema com base no ID fornecido",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Tema removido com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class, example = "Tema removido com sucesso")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tema não encontrado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Object.class)
                    )
            )
    })
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> removeTema(@PathVariable Long id) throws PermissaoNaoAutorizada {
        Resposta resposta = temaService.realizaDelete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("mensagem", resposta.getMensagem()));
    }

    @Operation(
            summary = "Lista todos os temas",
            description = "Retorna uma lista com todos os temas cadastrados",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Temas listados com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Tema.class))
                    )
            )
    })
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<TemaDTOResponse>> listaTemas() {
        List<Tema> temas = temaService.buscaTodosTemas();
        return ResponseEntity.ok(
                FormataRespostaGenerics.retornaListaFormatada(
                temas,
                t -> new TemaDTOResponse(t.getId(),
                        t.getDescricao()
                )
        ));
    }
}
