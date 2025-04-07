package com.blog.pessoal.acelera.maker.unit;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioDTO;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.PermissaoNaoAutorizada;
import com.blog.pessoal.acelera.maker.exception.UsuarioJaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Usuario;
import com.blog.pessoal.acelera.maker.repository.UsuarioRepository;
import com.blog.pessoal.acelera.maker.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioServiceImpl;

    @Test
    void testCriaUsuario() throws UsuarioJaExisteException {
        UsuarioDTO dto = new UsuarioDTO("Lucas", "lucas123", "senha123", null);

        usuarioServiceImpl.criaUsuario(dto);

        verify(usuarioRepository, times(1)).save(argThat(usuario ->
                usuario.getNome().equals("Lucas") &&
                        usuario.getUsuario().equals("lucas123")
        ));
    }

    @Test
    void criaUsuarioLancaExcecaoQuandoUsuarioJaExiste() {
        UsuarioDTO dto = new UsuarioDTO("Lucas", "lucas123", "senha123", null);

        when(usuarioRepository.existsByUsuario("lucas123"))
                .thenReturn(true); // <- simula existência

        assertThrows(UsuarioJaExisteException.class, () -> {
            usuarioServiceImpl.criaUsuario(dto);
        });

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testAtualizaDadosUsuario() throws UsuarioJaExisteException {
        Long id = 1L;



        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("Antigo");
        usuarioExistente.setUsuario("antigoUsuario");
        usuarioExistente.setSenha("senhaAntiga");
        usuarioExistente.setFoto("fotoAntiga");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(id);
        usuarioAtualizado.setNome("Novo Nome");
        usuarioAtualizado.setUsuario("novoUsuario");
        usuarioAtualizado.setSenha("novaSenha");
        usuarioAtualizado.setFoto("novaFoto");

        when(usuarioRepository.save(usuarioExistente)).thenReturn(usuarioAtualizado);
        usuarioServiceImpl.salvaUsuario(usuarioExistente);

        assertEquals("Novo Nome", usuarioAtualizado.getNome());
        assertEquals("novoUsuario", usuarioAtualizado.getUsuario());
        assertEquals("novaSenha", usuarioAtualizado.getSenha());
        assertEquals("novaFoto", usuarioAtualizado.getFoto());

        verify(usuarioRepository).save(any(Usuario.class));
    }


    @Test
    void testBuscaUsuarioPorId() {

        Usuario mockUser = new Usuario(1L, "Ana Luiza", "anaLuiza123", "senhaAnaLuiz123", null);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        Usuario result = usuarioServiceImpl.buscaUsuario(1L);
        assertEquals("Ana Luiza", result.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        Long id = 99L;

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            usuarioServiceImpl.deletaUsuario(id);
        });

        verify(usuarioRepository, never()).deleteById(any());
    }
}