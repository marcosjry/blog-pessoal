package com.blog.pessoal.acelera.maker.repository;

import com.blog.pessoal.acelera.maker.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long>, JpaSpecificationExecutor<Postagem> {

}
