package com.blog.pessoal.acelera.maker.DTO.usuario;

public record UsuarioLoginResponseDTO(
        Long usuarioId,
        String nome,
        String usuario,
        String foto,
        String token
) {
}
