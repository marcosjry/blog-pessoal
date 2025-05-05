package com.blog.pessoal.acelera.maker.DTO.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioLoginReqDTO(
        @NotBlank(message = "Usuario não pode estar em branco.")
        String usuario,
        @Size(min = 7, max = 100, message = "Senha com no mínimo 7 dígitos.")
        @NotBlank(message = "Senha não pode estar em branco.")
        String senha) {
}
