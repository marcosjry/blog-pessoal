package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioLoginDTO;
import com.blog.pessoal.acelera.maker.exception.UsuarioSenhaInvalidoException;
import com.blog.pessoal.acelera.maker.service.UsuarioLoginService;
import com.blog.pessoal.acelera.maker.util.FormataRespostaGenerics;
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
    public ResponseEntity<UsuarioLoginDTO> validaLogin(@Valid @RequestBody UsuarioLoginDTO usuarioLoginDTO) throws UsuarioSenhaInvalidoException {
        UsuarioLoginDTO usuario = usuarioLoginService.realizaLogin(usuarioLoginDTO);
        return ResponseEntity.accepted().body(usuario);
    }
}
