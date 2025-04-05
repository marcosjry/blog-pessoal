package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioDTO;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.IntegridadeVioladaException;
import com.blog.pessoal.acelera.maker.exception.UsuarioJaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/usuarios")
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<String> criaUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) throws UsuarioJaExisteException {
        Resposta resposta = usuarioService.realizarCadastro(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta.getMensagem());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizaDadosUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO usuarioDTO) throws UsuarioJaExisteException {
        Resposta resposta = usuarioService.realizaAtualizacao(id, usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta.getMensagem());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletaUsuario(@PathVariable Long id){
        Resposta resposta = usuarioService.realizaDelete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(resposta.getMensagem());
    }

    @GetMapping
    public ResponseEntity<String> entraSite() {
        return ResponseEntity.ok("Requisição aceita.");
    }
}
