package com.blog.pessoal.acelera.maker.repository;

import com.blog.pessoal.acelera.maker.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByUsuario(String usuario);

    Optional<Usuario> findByUsuario(String usuario);
}
