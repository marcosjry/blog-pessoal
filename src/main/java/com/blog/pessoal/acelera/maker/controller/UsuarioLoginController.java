package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioLoginDTO;
import com.blog.pessoal.acelera.maker.exception.UsuarioSenhaInvalidoException;
import com.blog.pessoal.acelera.maker.service.UsuarioLoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/usuarios/login")
public class UsuarioLoginController {

    @Autowired
    private UsuarioLoginService usuarioLoginService;

    @PostMapping
    public ResponseEntity<String> validaLogin(@Valid @RequestBody UsuarioLoginDTO usuarioLoginDTO) throws UsuarioSenhaInvalidoException {
        String token = usuarioLoginService.realizaLogin(usuarioLoginDTO);
        return ResponseEntity.accepted().body("bearer-token:" + token);
    }
}
