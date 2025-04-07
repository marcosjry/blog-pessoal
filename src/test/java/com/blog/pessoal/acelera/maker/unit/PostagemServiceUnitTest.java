package com.blog.pessoal.acelera.maker.unit;

import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemDTO;
import com.blog.pessoal.acelera.maker.DTO.postagem.PostagemUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.PermissaoNaoAutorizada;
import com.blog.pessoal.acelera.maker.model.Postagem;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Tema;
import com.blog.pessoal.acelera.maker.model.Usuario;
import com.blog.pessoal.acelera.maker.repository.PostagemRepository;
import com.blog.pessoal.acelera.maker.service.impl.PostagemServiceImpl;
import com.blog.pessoal.acelera.maker.service.impl.TemaServiceImpl;
import com.blog.pessoal.acelera.maker.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostagemServiceUnitTest {

    @Mock
    private PostagemRepository postagemRepository;

    @Mock
    private UsuarioServiceImpl usuarioService;

    @Mock
    private TemaServiceImpl temaServiceImpl;

    @Spy
    @InjectMocks
    private PostagemServiceImpl postagemService;

    @Test
    void testCriaPostagem() {
        Usuario usuario = new Usuario(1L, "Ana Luiza", "anaLuiza123", "senha", null);
        Tema tema = new Tema(1L, "Tema Teste");
        PostagemDTO dto = new PostagemDTO("Título Válido", "Texto Válido", tema.getId());

        when(usuarioService.buscaUsuario("anaLuiza123")).thenReturn(new Usuario());
        when(temaServiceImpl.buscaTema(tema.getId())).thenReturn(new Tema());

        Resposta resposta = postagemService.criarPostagem(dto, usuario.getUsuario());

        assertEquals("success", resposta.getStatus());
    }

    @Test
    void testAtualizaDadosPostagem() throws PermissaoNaoAutorizada {
        Usuario usuario = new Usuario(1L, "Ana Luiza", "anaLuiza123", "senha", null);
        Postagem postagem = new Postagem("Título Válido", "Texto Válido", new Tema(), usuario);
        postagem.setId(1L);

        PostagemUpdateDTO updateDTO = new PostagemUpdateDTO("Novo Título", "Novo Texto");

        when(postagemRepository.findById(1L)).thenReturn(Optional.of(postagem));
        when(usuarioService.buscaUsuario("anaLuiza123")).thenReturn(usuario);

        postagemService.atualizaPostagem(1L, updateDTO, "anaLuiza123");

        assertEquals("Novo Título", postagem.getTitulo());
        assertEquals("Novo Texto", postagem.getTexto());
    }

    @Test
    void testAtualizaPostagemLancaException() {
        Usuario usuarioOriginal = new Usuario(1L, "Ana Luiza", "anaLuiza123", "senha", null);
        Usuario outroUsuario = new Usuario(2L, "Outro", "outroUser", "senha2", null);
        Postagem postagem = new Postagem("Título", "Texto", new Tema(), usuarioOriginal);
        postagem.setId(1L);

        PostagemUpdateDTO updateDTO = new PostagemUpdateDTO("Novo Título", "Novo Texto");

        when(postagemRepository.findById(1L)).thenReturn(Optional.of(postagem));
        when(usuarioService.buscaUsuario("outroUser")).thenReturn(outroUsuario);

        assertThrows(PermissaoNaoAutorizada.class, () -> {
            postagemService.atualizaPostagem(1L, updateDTO, "outroUser");
        });
    }

    @Test
    void testRemovePostagem() throws PermissaoNaoAutorizada {
        Usuario usuario = new Usuario(1L, "Ana Oliveira", "anaOliveira", "senha", null);
        Postagem postagem = new Postagem("Título Válido", "Texto Válido", new Tema(), usuario);
        postagem.setId(1L);

        when(postagemRepository.findById(1L)).thenReturn(Optional.of(postagem));
        when(usuarioService.buscaUsuario("anaOliveira")).thenReturn(usuario);

        Resposta resposta = postagemService.removerPostagem(1L, usuario.getUsuario());

        assertEquals("success", resposta.getStatus());
    }

    @Test
    void testRemovePostagemLancaException() {
        Usuario usuario = new Usuario(1L, "Ana", "anaUser", "senha", null);
        Usuario outroUsuario = new Usuario(2L, "Outro", "outroUser", "senha2", null);
        Postagem postagem = new Postagem("Título", "Texto", new Tema(), usuario);
        postagem.setId(1L);

        when(postagemRepository.findById(1L)).thenReturn(Optional.of(postagem));
        when(usuarioService.buscaUsuario("outroUser")).thenReturn(outroUsuario);

        assertThrows(PermissaoNaoAutorizada.class, () -> {
            postagemService.removerPostagem(1L, "outroUser");
        });
    }

    @Test
    void testListaPostagens() {
        List<Postagem> mockList = List.of(
                new Postagem("Título 1", "Texto 1", new Tema(), new Usuario()),
                new Postagem("Título 2", "Texto 2", new Tema(), new Usuario())
        );

        when(postagemRepository.findAll()).thenReturn(mockList);

        List<Postagem> result = postagemService.buscaTodasPostagens();

        assertEquals(2, result.size());
    }

    @Test
    void testListaPostagensPorFiltro() {
        Usuario usuario1 = new Usuario(1L, "Ana", "anaUser", "senha", null);
        Tema tema = new Tema(2L, "Tema Teste");

        Postagem postagem1 = new Postagem("titulo valido 1", "texto valido 1", tema, usuario1);
        Postagem postagem2 = new Postagem("titulo valido 2", "texto valido 2", tema, usuario1);
        Postagem postagem3 = new Postagem("titulo valido 3", "texto valido 3", tema, usuario1);

        List<Postagem> postagensDoUsuario = List.of(postagem1, postagem2, postagem3);
        List<Postagem> postagensDoTema = List.of(postagem1, postagem2);

        when(postagemService.buscaPorFiltro(usuario1.getId(), null)).thenReturn(postagensDoUsuario);
        when(postagemService.buscaPorFiltro(null, 2L)).thenReturn(postagensDoTema);

        List<Postagem> result1 = postagemService.buscaPorFiltro(1L, null);

        List<Postagem> result2 = postagemService.buscaPorFiltro(null, 2L);

        assertEquals(3, result1.size());
        assertEquals(2, result2.size());
    }

}
