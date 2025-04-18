package com.blog.pessoal.acelera.maker.service.impl;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioDTO;
import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioUpdateDTO;
import com.blog.pessoal.acelera.maker.exception.PermissaoNaoAutorizada;
import com.blog.pessoal.acelera.maker.exception.UsuarioJaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Usuario;
import com.blog.pessoal.acelera.maker.repository.UsuarioRepository;
import com.blog.pessoal.acelera.maker.service.UsuarioService;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    public UsuarioServiceImpl(@Lazy PasswordEncoder passwordEncoder, @Lazy UsuarioRepository usuarioRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Resposta realizarCadastro(UsuarioDTO usuarioDTO) throws UsuarioJaExisteException {
        Resposta resposta = new Resposta();
        try {
            criaUsuario(usuarioDTO);
            resposta.setStatus("success");
            resposta.setMensagem("Usuário criado com sucesso.");
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro durante a criação do Usuário.");
        }
        return resposta;
    }

    public void criaUsuario(UsuarioDTO usuarioDTO) throws UsuarioJaExisteException {

        lancaExceptionSeUsuarioExiste(usuarioDTO.usuario());

        Usuario userToCreate = new Usuario();
        userToCreate.setUsuario(usuarioDTO.usuario());
        userToCreate.setNome(usuarioDTO.nome());
        userToCreate.setSenha(passwordEncoder.encode(usuarioDTO.senha()));
        userToCreate.setFoto(usuarioDTO.foto());
        usuarioRepository.save(userToCreate);
    }

    @Transactional
    @Override
    public Resposta realizaAtualizacao(Long id, UsuarioUpdateDTO usuarioDTO) throws UsuarioJaExisteException {
        atualizaCadastro(id, usuarioDTO);
        return new Resposta("Usuario Atualizado com Sucesso.", "success");
    }

    @Transactional
    @Override
    public Resposta realizaDelete(Long id, String usuario) throws PermissaoNaoAutorizada {
        Usuario usuarioAutenticado = buscaUsuario(usuario);
        if(!Objects.equals(usuarioAutenticado.getId(), id))
            throw new PermissaoNaoAutorizada("Usuário sendo deletado não é o mesmo realizando a operação.");

        deletaUsuario(id);
        return new Resposta("Usuário deletado com sucesso.", "success");
    }


    public void deletaUsuario(Long id) {
        Usuario usuario = buscaUsuario(id);
        usuarioRepository.deleteById(usuario.getId());
    }

    public UsuarioDTO atualizaCadastro(Long id, UsuarioUpdateDTO usuarioDTO) throws UsuarioJaExisteException {
        lancaExceptionSeUsuarioExiste(usuarioDTO.usuario());
        try {
            Usuario usuario = buscaUsuario(id);

            Usuario atualizado = atualizaPorCampo(usuarioDTO, usuario);

            Usuario usuarioAtualizado = usuarioRepository.save(atualizado);
            return new UsuarioDTO(
                    usuarioAtualizado.getNome(),
                    usuarioAtualizado.getUsuario(),
                    usuarioAtualizado.getSenha(),
                    usuarioAtualizado.getFoto()
            );
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new UsuarioJaExisteException("Usuário já existe. Tente novamente.");
        }
    }

    public Usuario atualizaPorCampo(UsuarioUpdateDTO usuarioDTO, Usuario usuario) {
        if(usuarioDTO.usuario() != null)
            usuario.setUsuario(usuarioDTO.usuario());
        if(usuarioDTO.senha() != null)
            usuario.setSenha(usuarioDTO.senha());
        if(usuarioDTO.foto()  != null)
            usuario.setFoto(usuarioDTO.foto());
        if(usuarioDTO.nome()  != null)
            usuario.setNome(usuarioDTO.nome());
        return usuario;
    }

    @Override
    public boolean verificaSeUsuarioExiste(String usuario) {
        return usuarioRepository.existsByUsuario(usuario);
    }

    @Override
    public Usuario buscaUsuario(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));
    }

    @Override
    public Usuario buscaUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));
    }

    @Override
    public void salvaUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    void lancaExceptionSeUsuarioExiste(String usuario) throws UsuarioJaExisteException {
        boolean existeUsuario = verificaSeUsuarioExiste(usuario);
        if(existeUsuario)
            throw new UsuarioJaExisteException("Usuário já existe. Tente novamente.");
    }
}
