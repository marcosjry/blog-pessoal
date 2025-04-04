package com.blog.pessoal.acelera.maker.DTO.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDTO(

        @Size(min = 5, max = 100, message = "Digite o nome completo.")
        @NotBlank(message = "Nome não pode estar em branco.")
        String nome,

        @Size(min = 7, max = 100, message = "O Usuario deve ter no mínimo 7 caracteres")
        @NotBlank(message = "Usuario não pode estar em branco.")
        String usuario,

        @Size(min = 8, max = 100, message = "A senha deve ter no mínimo 8 caracteres")
        @NotBlank(message = "Senha não pode estar em branco.")
        String senha,
        String foto)
{ }
