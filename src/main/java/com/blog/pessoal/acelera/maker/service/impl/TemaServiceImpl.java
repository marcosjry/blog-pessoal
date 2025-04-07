package com.blog.pessoal.acelera.maker.service.impl;

import com.blog.pessoal.acelera.maker.DTO.tema.TemaDTO;
import com.blog.pessoal.acelera.maker.exception.TemaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Tema;
import com.blog.pessoal.acelera.maker.repository.TemaRepository;
import com.blog.pessoal.acelera.maker.service.TemaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TemaServiceImpl implements TemaService {

    @Autowired
    private TemaRepository temaRepository;

    public void criaTema(TemaDTO temaDTO) throws TemaExisteException, RuntimeException {
        lancaExceptionSeTemaExiste(temaDTO.descricao());
        try {
            Tema temaToCreate = new Tema();
            temaToCreate.setDescricao(temaDTO.descricao());
            temaRepository.save(temaToCreate);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro durante a criação do tema.");
        }
    }

    @Transactional
    @Override
    public Resposta criarTema(TemaDTO temaDTO) throws TemaExisteException {
        criaTema(temaDTO);
        return new Resposta("Tema criado com sucesso.", "success");
    }

    @Transactional
    @Override
    public Resposta atualiza(Long id, TemaDTO temaDTO) throws TemaExisteException {
        atualizaTema(id, temaDTO);
        return new Resposta("Tema atualizado com sucesso.", "success");
    }

    @Transactional
    @Override
    public Resposta realizaDelete(Long id) {
        deletaTema(id);
        return new Resposta("Tema removido com sucesso.", "success");
    }

    public void deletaTema(Long id) {
        Tema tema = buscaTema(id);
        temaRepository.deleteById(tema.getId());
    }

    @Override
    public Tema buscaTema(Long id) {
        return temaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Tema não encontrado."));
    }

    public void atualizaTema(Long id,TemaDTO temaDTO) throws TemaExisteException {
        lancaExceptionSeTemaExiste(temaDTO.descricao());

        Tema tema = buscaTema(id);
            tema.setDescricao(temaDTO.descricao());
            temaRepository.save(tema);
    }

    void lancaExceptionSeTemaExiste(String descricao) throws TemaExisteException {
        boolean existeDescricao = existsByDescricao(descricao);
        if(existeDescricao)
            throw new TemaExisteException("Tema já existe. Tente novamente.");
    }

    @Override
    public List<Tema> buscaTodosTemas() {
        return buscaTemas();
    }

    @Override
    public boolean existsByDescricao(String descricao) {
        return temaRepository.existsByDescricao(descricao).orElseThrow(() -> new NoSuchElementException("Nenhuma descrição encontrada."));
    }

    public List<Tema> buscaTemas(){
        return temaRepository.findAll();
    }

}
