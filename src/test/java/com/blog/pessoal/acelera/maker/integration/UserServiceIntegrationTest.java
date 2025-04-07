package com.blog.pessoal.acelera.maker.integration;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioDTO;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.UsuarioJaExisteException;
import com.blog.pessoal.acelera.maker.model.Usuario;
import com.blog.pessoal.acelera.maker.repository.UsuarioRepository;
import com.blog.pessoal.acelera.maker.service.TokenService;
import com.blog.pessoal.acelera.maker.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

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
    void devePermitirQueUsuarioSeAutoRemova() throws Exception {
        // 1. Cria e salva usuário
        Usuario usuario = new Usuario("Usuario teste", "usuarioTeste", "senhaDeTeste123", "");
        usuarioRepository.save(usuario);

        // 2. Gera token com base no login do usuário
        String token = tokenService.generateToken(usuario.getUsuario()); // Você pode ter esse método no TokenService

        // 3. Executa DELETE passando o ID correto do próprio usuário
        mockMvc.perform(delete("/api/usuarios/" + usuario.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isAccepted());
    }
}
