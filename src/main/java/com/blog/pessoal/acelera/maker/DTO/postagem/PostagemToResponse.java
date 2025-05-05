package com.blog.pessoal.acelera.maker.DTO.postagem;

import java.time.LocalDate;

public record PostagemToResponse(Long id, String texto, String titulo, Long usuarioId, String usuario, String tema, LocalDate dataPostagem) {
}
