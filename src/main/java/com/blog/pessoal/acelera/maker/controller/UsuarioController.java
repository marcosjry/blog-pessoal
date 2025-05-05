package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioDTO;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.PermissaoNaoAutorizada;
import com.blog.pessoal.acelera.maker.exception.UsuarioJaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.service.UsuarioService;

import com.blog.pessoal.acelera.maker.util.CapturaSubject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários. Cadastro, atualização e remoção.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Cadastrar um novo usuário",
            description = "Cria um novo usuário no sistema. Não requer autenticação."
    )
    @PostMapping
    public ResponseEntity<Map<String, String>> criaUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) throws UsuarioJaExisteException {
        Resposta resposta = usuarioService.realizarCadastro(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", resposta.getMensagem()));
    }

    @Operation(
            summary = "Atualizar dados do usuário",
            description = "Atualiza os dados de um usuário existente. Requer autenticação via token JWT."
    )
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizaDadosUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO usuarioDTO) throws UsuarioJaExisteException {
        Resposta resposta = usuarioService.realizaAtualizacao(id, usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", resposta.getMensagem()));
    }

    @Operation(
            summary = "Deletar um usuário",
            description = "Remove permanentemente um usuário do sistema. Requer autenticação via token JWT."
    )

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletaUsuario(@PathVariable Long id) throws PermissaoNaoAutorizada {
        String usuario = CapturaSubject.captura();
        Resposta resposta = usuarioService.realizaDelete(id, usuario);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("mensagem", resposta.getMensagem()));
    }

    

}
