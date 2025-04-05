package com.blog.pessoal.acelera.maker.DTO.usuario;

import com.blog.pessoal.acelera.maker.util.DeserializadorStringParaNull;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Size;

public record UsuarioLoginDTO(
        @JsonDeserialize(using = DeserializadorStringParaNull.class)
        Long id,

        @JsonDeserialize(using = DeserializadorStringParaNull.class)
        String nome,

        @JsonDeserialize(using = DeserializadorStringParaNull.class)
        @Size(min = 7, max = 100, message = "O Usuario deve ter no mínimo 7 caracteres.")
        String usuario,

        @JsonDeserialize(using = DeserializadorStringParaNull.class)
        @Size(min = 8, max = 100, message = "a Senha deve ter no mínimo 8 caracteres.")
        String senha,

        @JsonDeserialize(using = DeserializadorStringParaNull.class)
        String foto,

        @JsonDeserialize(using = DeserializadorStringParaNull.class)
        String token
) {}
