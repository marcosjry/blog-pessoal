package com.blog.pessoal.acelera.maker.DTO.postagem;

import com.blog.pessoal.acelera.maker.util.DeserializadorStringParaNull;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Size;

public record PostagemUpdateDTO(

        @JsonDeserialize(using = DeserializadorStringParaNull.class)
        @Size(min = 5, max = 100, message = "Digite no mínimo 5 caracteres.")
        String titulo,

        @JsonDeserialize(using = DeserializadorStringParaNull.class)
        @Size(min = 5, max = 300, message = "Digite no mínimo 5 caracteres.")
        String texto,

        Long temaId

) { }

