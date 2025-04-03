package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioDTO;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/usuarios")
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<String> criaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Resposta resposta = usuarioService.realizarCadastro(usuarioDTO);
        return switch (resposta.getStatus()) {
            case "success" -> ResponseEntity.status(HttpStatus.CREATED).body(resposta.getMensagem());
            case "conflict" -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(resposta.getMensagem());
            default ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Entre em contato com os desenvolvedores.");
        };
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizaDadosUsuario(@PathVariable Integer id, @RequestBody UsuarioDTO usuarioDTO){
        Resposta resposta = usuarioService.realizaAtualizacao(id, usuarioDTO);
        return switch (resposta.getStatus()) {
            case "success" -> ResponseEntity.status(HttpStatus.CREATED).body(resposta.getMensagem());
            case "conflict" -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(resposta.getMensagem());
            case "error" -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(resposta.getMensagem());
            case "not found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta.getMensagem());
            default ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Entre em contato com os desenvolvedores.");
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletaUsuario(@PathVariable Integer id){
        Resposta resposta = usuarioService.realizaDelete(id);
        return switch (resposta.getStatus()) {
            case "success" -> ResponseEntity.status(HttpStatus.CREATED).body(resposta.getMensagem());
            case "error" -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(resposta.getMensagem());
            case "not found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta.getMensagem());
            default ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Entre em contato com os desenvolvedores.");
        };
    }

    @GetMapping
    public ResponseEntity<String> entraSite() {
        return ResponseEntity.ok("Requisição aceita.");
    }
}
