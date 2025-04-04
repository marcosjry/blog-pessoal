package com.blog.pessoal.acelera.maker.service;

import com.blog.pessoal.acelera.maker.DTO.tema.TemaDTO;
import com.blog.pessoal.acelera.maker.exception.TemaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Tema;

import java.util.List;

public interface TemaService {

    Resposta criarTema(TemaDTO temaDTO) throws TemaExisteException, RuntimeException;

    Resposta atualiza(Long id, TemaDTO temaDTO) throws TemaExisteException;

    Resposta realizaDelete(Long id);

    List<Tema> buscaTodosTemas();

    Tema buscaTema(Long id);
}
