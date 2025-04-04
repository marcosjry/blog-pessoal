package com.blog.pessoal.acelera.maker.DTO.usuario;

import com.blog.pessoal.acelera.maker.util.DeserializadorStringParaNull;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(

        @JsonDeserialize(using = DeserializadorStringParaNull.class)
        @Size(min = 5, max = 100, message = "Digite o nome completo.")
        String nome,

        @JsonDeserialize(using = DeserializadorStringParaNull.class)
        @Size(min = 7, max = 100, message = "O Usuario deve no mínimo 7 caracteres.")
        String usuario,

        @JsonDeserialize(using = DeserializadorStringParaNull.class)
        @Size(min = 8, max = 100, message = "A senha deve ter no mínimo 8 caracteres")
        String senha,

        String foto) {
}
