package com.blog.pessoal.acelera.maker.integration;

import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemDTO;
import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.PermissaoNaoAutorizada;
import com.blog.pessoal.acelera.maker.model.Postagem;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Tema;
import com.blog.pessoal.acelera.maker.model.Usuario;
import com.blog.pessoal.acelera.maker.repository.PostagemRepository;
import com.blog.pessoal.acelera.maker.repository.TemaRepository;
import com.blog.pessoal.acelera.maker.repository.UsuarioRepository;
import com.blog.pessoal.acelera.maker.service.PostagemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class PostagemServiceIntegrationTest {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private PostagemService postagemService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TemaRepository temaRepository;

    @Test
    void testCriaPostagem() {
        Usuario mockUsuario = new Usuario(null, "Ana Luiza", "anaLuiza123", "senhaAnaLuiz123", null);
        mockUsuario = usuarioRepository.save(mockUsuario);

        Tema mockTema = new Tema(null, "Tema Teste");
        mockTema = temaRepository.save(mockTema);

        PostagemDTO postagemDTO = new PostagemDTO("Teste Postagem", "testando integração", mockTema.getId());
        Resposta resposta = postagemService.criarPostagem(postagemDTO, mockUsuario.getUsuario());

        assertEquals("success", resposta.getStatus());
    }

    @Test
    void testAtualizaDadosPostagem() throws PermissaoNaoAutorizada {
        Usuario mockUsuario = new Usuario(null, "Ana Luiza", "anaLuiza123", "senhaAnaLuiz123", null);
        mockUsuario = usuarioRepository.save(mockUsuario);

        Tema mockTema = new Tema(null, "Tema Teste");
        mockTema = temaRepository.save(mockTema);

        Postagem postagem = postagemRepository.save(new Postagem("teste titulo", "teste texto", mockTema, mockUsuario));
        PostagemUpdateDTO postagemUpdateDTO = new PostagemUpdateDTO("Novo teste titulo", "novo teste texto");
        postagemService.atualizaPostagem(postagem.getId(), postagemUpdateDTO, mockUsuario.getUsuario());

        postagem = postagemRepository.findById(postagem.getId()).orElseThrow();

        assertEquals("Novo teste titulo", postagem.getTitulo());
        assertEquals("novo teste texto", postagem.getTexto());
    }

    @Test
    void testAtualizaPostagemLancaException() throws PermissaoNaoAutorizada {
        Usuario mockUsuario = new Usuario(null, "Ana Luiza", "anaLuiza123", "senhaAnaLuiz123", null);
        mockUsuario = usuarioRepository.save(mockUsuario);

        Tema mockTema = new Tema(null, "Tema Teste");
        mockTema = temaRepository.save(mockTema);

        Usuario mockUsuario2 = new Usuario(null, "Usuario Teste", "testeUsuarioDif", "testeUsuarioDif123", null);
        usuarioRepository.save(mockUsuario2);

        Postagem postagem = postagemRepository.save(new Postagem("teste titulo", "teste texto", mockTema, mockUsuario));
        PostagemUpdateDTO postagemUpdateDTO = new PostagemUpdateDTO("Novo teste titulo", "novo teste texto");

        assertThrows(PermissaoNaoAutorizada.class, () -> {
            postagemService.atualizaPostagem(postagem.getId(), postagemUpdateDTO, mockUsuario2.getUsuario());
        });
    }

    @Test
    void testRemovePostagem() throws PermissaoNaoAutorizada {
        Usuario mockUsuario = new Usuario(null, "Ana Luiza", "anaLuiza123", "senhaAnaLuiz123", null);
        mockUsuario = usuarioRepository.save(mockUsuario);

        Tema mockTema = new Tema(null, "Tema Teste");
        mockTema = temaRepository.save(mockTema);

        Postagem postagem = postagemRepository.save(new Postagem("teste titulo", "teste texto", mockTema, mockUsuario));

        Resposta resposta = postagemService.removerPostagem(postagem.getId(), mockUsuario.getUsuario());

        assertEquals("success", resposta.getStatus());
    }

    @Test
    void testRemovePostagemLancaException() {
        Usuario mockUsuario = new Usuario(null, "Ana Luiza", "anaLuiza123", "senhaAnaLuiz123", null);
        mockUsuario = usuarioRepository.save(mockUsuario);

        Usuario mockUsuario2 = new Usuario(null, "Usuario Teste", "testeUsuarioDif", "testeUsuarioDif123", null);
        usuarioRepository.save(mockUsuario2);

        Tema mockTema = new Tema(null, "Tema Teste");
        mockTema = temaRepository.save(mockTema);

        Postagem postagem = postagemRepository.save(new Postagem("teste titulo", "teste texto", mockTema, mockUsuario));

        assertThrows(PermissaoNaoAutorizada.class, () -> {
            postagemService.removerPostagem(postagem.getId(), "testeUsuarioDif");
        });
    }

    @Test
    void testListaPostagens() {

        Usuario mockUsuario = new Usuario(null, "Ana Luiza", "anaLuiza123", "senha123", null);
        mockUsuario = usuarioRepository.save(mockUsuario);

        Tema tema = new Tema(null, "Tema Teste");
        tema = temaRepository.save(tema);

        postagemRepository.save(new Postagem("Título 1", "Texto 1", tema, mockUsuario));
        postagemRepository.save(new Postagem("Título 2", "Texto 2", tema, mockUsuario));
        postagemRepository.save(new Postagem("Título 3", "Texto 3", tema, mockUsuario));
        postagemRepository.save(new Postagem("Título 4", "Texto 4", tema, mockUsuario));


        List<Postagem> postagens = postagemService.buscaTodasPostagens();

        assertEquals(4, postagens.size());
        assertTrue(postagens.stream().anyMatch(p -> p.getTitulo().equals("Título 1")));
        assertTrue(postagens.stream().anyMatch(p -> p.getTitulo().equals("Título 2")));
    }

    @Test
    void testListaPostagensPorFiltro() {

        Usuario mockUsuario = new Usuario(null, "Ana Luiza", "anaLuiza123", "senha123", null);
        mockUsuario = usuarioRepository.save(mockUsuario);

        Usuario mockUsuario2 = new Usuario(null, "Usuario Teste", "testeUsuarioDif", "testeUsuarioDif123", null);
        usuarioRepository.save(mockUsuario2);

        Tema tema = new Tema(null, "Tema Teste");
        tema = temaRepository.save(tema);


        postagemRepository.save(new Postagem("Título 1", "Texto 1", tema, mockUsuario));
        postagemRepository.save(new Postagem("Título 2", "Texto 2", tema, mockUsuario));
        postagemRepository.save(new Postagem("Título 3", "Texto 3", tema, mockUsuario2));

        List<Postagem> postagensF1 = postagemService.buscaPorFiltro(mockUsuario.getId(), null);
        List<Postagem> postagensF2 = postagemService.buscaPorFiltro(null, tema.getId());

        assertEquals(2, postagensF1.size());
        assertEquals(3, postagensF2.size());
    }
}
