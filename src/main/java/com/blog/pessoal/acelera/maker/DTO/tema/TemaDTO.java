package com.blog.pessoal.acelera.maker.DTO.tema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TemaDTO(
        @NotBlank(message = "Tema não pode estar em branco.")
        @Size(min = 6, max = 50, message = "Tamanha mínimo de 6 caracteres.")
        String descricao) {
}
