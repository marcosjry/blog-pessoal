package com.blog.pessoal.acelera.maker.integration;

import com.blog.pessoal.acelera.maker.DTO.tema.TemaDTO;
import com.blog.pessoal.acelera.maker.exception.TemaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Tema;
import com.blog.pessoal.acelera.maker.repository.TemaRepository;
import com.blog.pessoal.acelera.maker.service.TemaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class TemaServiceIntegrationTest {

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private TemaService temaService;

    @Test
    void testCriaTema() throws TemaExisteException {
        TemaDTO temaDTO = new TemaDTO("Teste tema");
        Resposta resposta = temaService.criarTema(temaDTO);

        assertEquals("success", resposta.getStatus());
    }

    @Test
    void testCriaTemaLancaException() throws TemaExisteException {
        TemaDTO temaDTO = new TemaDTO("Teste tema");
        Resposta resposta = temaService.criarTema(temaDTO);
        TemaDTO temaDTO2 = new TemaDTO("Teste tema");

        assertEquals("success", resposta.getStatus());
        assertThrows(TemaExisteException.class, () -> {
            temaService.criarTema(temaDTO2);
        });
    }

    @Test
    void testAtualizaTema() throws TemaExisteException {
        Tema tema = new Tema("Teste tema");
        tema = temaRepository.save(tema);

        TemaDTO temaDTO2 = new TemaDTO("Novo Teste tema");

        Resposta resposta = temaService.atualiza(tema.getId(), temaDTO2);

        assertEquals("success", resposta.getStatus());

    }

    @Test
    void testAtualizaTemaLancaException() {
        Tema tema = new Tema("Teste tema");
        temaRepository.save(tema);

        Tema tema2 = new Tema("Teste tema 2");
        Tema novotema = temaRepository.save(tema2);
        Long id = novotema.getId();

        TemaDTO temaDTO2 = new TemaDTO("Teste tema");

        assertThrows(TemaExisteException.class, () -> {
            temaService.atualiza(id, temaDTO2);
        });

    }

    @Test
    void testRemoveTema() {
        Tema tema = new Tema("Teste tema");
        tema = temaRepository.save(tema);

        Long id = tema.getId();

        Resposta resposta = temaService.realizaDelete(id);

        assertEquals("success", resposta.getStatus());
    }

    @Test
    void testListaTodosTemas() {
        temaRepository.save(new Tema("Teste tema"));
        temaRepository.save(new Tema("Teste tema 2"));
        temaRepository.save(new Tema("Teste tema 3"));

        List<Tema> temas = temaService.buscaTodosTemas();

        assertEquals(3, temas.size());
        assertTrue(temas.stream().anyMatch(p -> p.getDescricao().equals("Teste tema")));
        assertTrue(temas.stream().anyMatch(p -> p.getDescricao().equals("Teste tema 3")));
    }
}
