package com.blog.pessoal.acelera.maker.DTO.postagem;

import java.time.LocalDate;

public record PostsByDate(
        LocalDate data,
        Long qtd_postagens
) {
}
