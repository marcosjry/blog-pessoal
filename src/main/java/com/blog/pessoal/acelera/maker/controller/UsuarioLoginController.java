package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioLoginDTO;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioLoginReqDTO;
import com.blog.pessoal.acelera.maker.exception.UsuarioSenhaInvalidoException;
import com.blog.pessoal.acelera.maker.service.UsuarioLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/usuarios/login")
@Tag(name = "Login Usuário", description = "Endpoints para validar login de usuários.")
public class UsuarioLoginController {

    @Autowired
    private UsuarioLoginService usuarioLoginService;

    @Operation(
            summary = "Login Usuário",
            description = "Autentica um usuário, retornando um token JWT."
    )
    @PostMapping
    public ResponseEntity<UsuarioLoginDTO> validaLogin(@Valid @RequestBody UsuarioLoginReqDTO usuarioLoginDTO) throws UsuarioSenhaInvalidoException {
        UsuarioLoginDTO usuario = usuarioLoginService.realizaLogin(usuarioLoginDTO);
        return ResponseEntity.accepted().body(usuario);
    }

}
