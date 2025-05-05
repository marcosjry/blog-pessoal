package com.blog.pessoal.acelera.maker.repository;

import com.blog.pessoal.acelera.maker.DTO.postagem.PostStatsByUserDTO;
import com.blog.pessoal.acelera.maker.DTO.postagem.PostsByDate;
import com.blog.pessoal.acelera.maker.model.Postagem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long>, JpaSpecificationExecutor<Postagem> {

    @Query("SELECT new com.blog.pessoal.acelera.maker.DTO.postagem.PostStatsByUserDTO(p.usuario.nome, COUNT(p)) FROM Postagem p GROUP BY p.usuario.nome")
    List<PostStatsByUserDTO> countPostsPerUser();

    @Query("SELECT new com.blog.pessoal.acelera.maker.DTO.postagem.PostsByDate(p.data, COUNT(p)) FROM Postagem p GROUP BY p.data")
    List<PostsByDate> lastPostsByQuery(Pageable pageable);

    @Query("SELECT COUNT(p) FROM Postagem p")
    Long countNumOfPosts();

}
