package com.blog.pessoal.acelera.maker.integration;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioDTO;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.UsuarioJaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Usuario;
import com.blog.pessoal.acelera.maker.repository.UsuarioRepository;
import com.blog.pessoal.acelera.maker.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void testCriaUsuario() throws UsuarioJaExisteException {
        UsuarioDTO mockUser = new UsuarioDTO("Ana Luiza", "anaLuiza123", "senhaAnaLuiz123", null);

        usuarioService.realizarCadastro(mockUser);

        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsuario("anaLuiza123");

        assertTrue(usuarioOptional.isPresent());
        Usuario usuario = usuarioOptional.get();
        assertEquals("Ana Luiza", usuario.getNome());
    }

    @Test
    void testBuscaUsuarioPorId() {
        Usuario mockUsuario = new Usuario( "Ana Luiza", "anaLuiz123", "senhaAnaLuiz123", null);
        usuarioRepository.save(mockUsuario);

        Usuario result = usuarioService.buscaUsuario(mockUsuario.getId());

        assertEquals("Ana Luiza", result.getNome());
    }

    @Test
    void testBuscaUsuarioPorUsuario() {
        Usuario mockUsuario = new Usuario( "Ana Luiza", "anaLuiz123", "senhaAnaLuiz123", null);
        usuarioRepository.save(mockUsuario);

        Usuario result = usuarioService.buscaUsuario(mockUsuario.getUsuario());

        assertEquals("Ana Luiza", result.getNome());
    }

    @Test
    void atualizaCadastroDeveAtualizarComSucesso() throws UsuarioJaExisteException {

        UsuarioDTO mockUser = new UsuarioDTO("Ana Luiza", "anaLuiza123", "senhaAnaLuiz123", null);
        usuarioService.realizarCadastro(mockUser);

        Usuario result = usuarioService.buscaUsuario(mockUser.usuario());

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(
                "Nome Novo", "usuario123", "", "novaFoto.png"
        );

        usuarioService.realizaAtualizacao(result.getId(), dto);

        assertEquals("Nome Novo", result.getNome());
        assertEquals("usuario123", result.getUsuario());
        assertEquals("novaFoto.png", result.getFoto());
    }

    @Test
    void atualizaCadastroDeveLancarException() throws UsuarioJaExisteException {
        UsuarioDTO mock1 = new UsuarioDTO("Ana Luiza", "anaLuiza123", "senhaAnaLuiz123", null);
        usuarioService.realizarCadastro(mock1);

        UsuarioDTO mock2 = new UsuarioDTO("Roberta Santos", "robertaSantos", "robertaSantos123", null);
        usuarioService.realizarCadastro(mock2);

        Usuario usuario = usuarioService.buscaUsuario(mock2.usuario());

        UsuarioUpdateDTO updateDTO = new UsuarioUpdateDTO("Roberta S.", "anaLuiza123", "12345678", "");

        assertThrows(UsuarioJaExisteException.class, () -> {
            usuarioService.realizaAtualizacao(usuario.getId(), updateDTO);
        });
    }

    @Test
    void removeUsuario() throws UsuarioJaExisteException {
        UsuarioDTO mock1 = new UsuarioDTO("Ana Luiza", "anaLuiza123", "senhaAnaLuiz123", null);
        usuarioService.realizarCadastro(mock1);

        Usuario usuario = usuarioService.buscaUsuario(mock1.usuario());

        Resposta resposta = usuarioService.realizaDelete(usuario.getId());

        assertEquals("success", resposta.getStatus());
    }

}
