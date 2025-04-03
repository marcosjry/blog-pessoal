package com.blog.pessoal.acelera.maker.service.impl;

import com.blog.pessoal.acelera.maker.DTO.usuario.UsuarioDTO;
import com.blog.pessoal.acelera.maker.exception.UsuarioJaExiste;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Usuario;
import com.blog.pessoal.acelera.maker.repository.UsuarioRepository;
import com.blog.pessoal.acelera.maker.service.UsuarioService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Resposta realizarCadastro(UsuarioDTO usuarioDTO) {
        Resposta resposta = new Resposta();
        try {
            criaUsuario(usuarioDTO);
            resposta.setStatus("success");
            resposta.setMensagem("Usuário criado com sucesso.");
        } catch (UsuarioJaExiste e) {
            resposta.setMensagem(e.getMessage());
            resposta.setStatus("conflict");
        } catch (Exception e) {
            resposta.setMensagem(e.getMessage());
            resposta.setStatus("error");
        }
        return resposta;
    }

    public void criaUsuario(UsuarioDTO usuarioDTO) throws UsuarioJaExiste {

        boolean ehValido = validaUsuarioDTO(usuarioDTO);
        if(!ehValido)
            throw new InvalidParameterException("Parâmetros Vazio ou inválido.");

        boolean existe = verificaSeUsuarioExiste(usuarioDTO.usuario());
        if(existe)
            throw new UsuarioJaExiste("Usuario já registrado.");

        Usuario userToCreate = new Usuario();
        userToCreate.setUsuario(usuarioDTO.usuario());
        userToCreate.setNome(usuarioDTO.nome());
        userToCreate.setSenha(usuarioDTO.senha());
        userToCreate.setFoto(usuarioDTO.foto());
        usuarioRepository.save(userToCreate);
    }

    @Override
    public Resposta realizaAtualizacao(Integer id, UsuarioDTO usuarioDTO) {
        try {
            atualizaCadastro(id, usuarioDTO);
            return new Resposta("Usuario Atualizado com Sucesso", "success");
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            return new Resposta("Usuário indisponível, tente novamente.", "conflict");
        } catch (NoSuchElementException e) {
            return new Resposta(e.getMessage(), "not found");
        } catch (Exception e) {
            return new Resposta("Erro durante a atualização do Usuário.", "error");
        }
    }

    @Override
    public Resposta realizaDelete(Integer id) {
        try {
            deletaUsuario(id);
            return new Resposta("Usuário deletado com sucesso.", "success");
        } catch (NoSuchElementException e) {
            return new Resposta(e.getMessage(), "not found");
        } catch (Exception e) {
            return new Resposta(e.getMessage(), "error");
        }
    }


    public void deletaUsuario(Integer id) {
        Usuario usuario = buscaUsuario(id);
        usuarioRepository.deleteById(usuario.getId());
    }


    @Transactional
    public UsuarioDTO atualizaCadastro(Integer id, UsuarioDTO usuarioDTO) throws ConstraintViolationException {
        Usuario usuario = buscaUsuario(id);

        Usuario atualizado = atualizaPorCampo(usuarioDTO, usuario);

        Usuario usuarioAtualizado = usuarioRepository.save(atualizado);
        return new UsuarioDTO(
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getUsuario(),
                usuarioAtualizado.getSenha(),
                usuarioAtualizado.getFoto()
        );
    }

    public Usuario atualizaPorCampo(UsuarioDTO usuarioDTO, Usuario usuario) {
        if(!usuarioDTO.usuario().isEmpty() && !usuarioDTO.usuario().isBlank())
            usuario.setUsuario(usuarioDTO.usuario());
        if(!usuarioDTO.senha().isEmpty() && !usuarioDTO.senha().isBlank())
            usuario.setSenha(usuarioDTO.senha());
        if(!usuarioDTO.foto().isEmpty() && !usuarioDTO.foto().isBlank())
            usuario.setFoto(usuarioDTO.foto());
        return usuario;
    }

    public boolean validaUsuarioDTO(UsuarioDTO usuarioDTO) {
        return !usuarioDTO.usuario().isEmpty()
                && !usuarioDTO.senha().isEmpty()
                && !usuarioDTO.nome().isEmpty()
                && !usuarioDTO.nome().isBlank()
                && !usuarioDTO.senha().isBlank()
                && !usuarioDTO.usuario().isBlank();
    }

    @Override
    public boolean verificaSeUsuarioExiste(String usuario) {
        return usuarioRepository.existsByUsuario(usuario);
    }

    @Override
    public Usuario buscaUsuario(Integer id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));
    }

}
