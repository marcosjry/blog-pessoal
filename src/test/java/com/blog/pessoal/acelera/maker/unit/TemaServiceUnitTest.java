package com.blog.pessoal.acelera.maker.unit;

import com.blog.pessoal.acelera.maker.DTO.tema.TemaDTO;
import com.blog.pessoal.acelera.maker.exception.TemaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Tema;
import com.blog.pessoal.acelera.maker.repository.TemaRepository;
import com.blog.pessoal.acelera.maker.service.impl.TemaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TemaServiceUnitTest {

    @Mock
    private TemaRepository temaRepository;

    @Spy
    @InjectMocks
    private TemaServiceImpl temaService;

    @Test
    void testCriaTemaComSucesso() throws TemaExisteException {
        TemaDTO temaDTO = new TemaDTO("Teste tema");

        when(temaRepository.existsByDescricao("Teste tema")).thenReturn(Optional.of(false));

        Resposta resposta = temaService.criarTema(temaDTO);

        assertEquals("success", resposta.getStatus());
        verify(temaRepository).save(any(Tema.class));
    }

    @Test
    void testCriaTemaLancaExceptionQuandoTemaJaExiste() {
        TemaDTO temaDTO = new TemaDTO("Teste tema");
        Tema temaExistente = new Tema("Teste tema");

        when(temaRepository.existsByDescricao("Teste tema")).thenReturn(Optional.of(true));

        assertThrows(TemaExisteException.class, () -> {
            temaService.criarTema(temaDTO);
        });

        verify(temaRepository, never()).save(any());
    }

    @Test
    void testAtualizaTemaComSucesso() throws TemaExisteException {
        Long id = 1L;
        Tema temaAtual = new Tema("Tema Antigo");
        temaAtual.setId(id);

        TemaDTO novoTema = new TemaDTO("Tema Atualizado");

        when(temaRepository.findById(id)).thenReturn(Optional.of(temaAtual));
        when(temaRepository.existsByDescricao("Tema Atualizado")).thenReturn(Optional.of(false));

        Resposta resposta = temaService.atualiza(id, novoTema);

        assertEquals("success", resposta.getStatus());
        assertEquals("Tema Atualizado", temaAtual.getDescricao());
    }

    @Test
    void testAtualizaTemaLancaExceptionSeDescricaoJaExiste() {


        Tema temaParaAtualizar = new Tema(1L,"Tema Original");

        Tema temaExistente = new Tema( 2L,"Tema Existente");

        when(temaRepository.existsByDescricao(temaExistente.getDescricao())).thenReturn(Optional.of(true));

        TemaDTO dto = new TemaDTO("Tema Existente");

        Long id = temaParaAtualizar.getId();
        assertThrows(TemaExisteException.class, () -> {
            temaService.atualiza(id, dto);
        });
    }

    @Test
    void testRemoveTemaComSucesso() {
        Tema tema = new Tema(1L, "Tema para remover");
        Long id = tema.getId();


        when(temaRepository.findById(id)).thenReturn(Optional.of(tema));

        Resposta resposta = temaService.realizaDelete(id);

        assertEquals("success", resposta.getStatus());
        verify(temaRepository).deleteById(id);
    }

    @Test
    void testBuscaTodosTemas() {
        var temasMock = List.of(
                new Tema("Tema 1"),
                new Tema("Tema 2"),
                new Tema("Tema 3")
        );

        when(temaRepository.findAll()).thenReturn(temasMock);

        List<Tema> result = temaService.buscaTodosTemas();

        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(t -> t.getDescricao().equals("Tema 1")));
        assertTrue(result.stream().anyMatch(t -> t.getDescricao().equals("Tema 3")));
    }
}
