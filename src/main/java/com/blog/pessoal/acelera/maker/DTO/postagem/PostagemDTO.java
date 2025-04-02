package com.blog.pessoal.acelera.maker.DTO.postagem;

import java.util.Date;

public record PostagemDTO(String titulo, String texto, Date data, Integer temaId, Integer userId) {
}
