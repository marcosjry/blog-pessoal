package com.blog.pessoal.acelera.maker.DTO.postagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostagemDTO(
        @Size(min = 5, max = 100, message = "Digite no mínimo 5 caracteres.")
        @NotBlank(message = "Titulo não pode estar em branco.")
        String titulo,

        @Size(min = 5, max = 300, message = "Digite no mínimo 5 caracteres.")
        @NotBlank(message = "Texto não pode estar em branco.")
        String texto,

        @NotNull(message = "Tema não pode ser nulo.")
        Long temaId) {
}
