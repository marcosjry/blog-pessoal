package com.blog.pessoal.acelera.maker.repository;

import com.blog.pessoal.acelera.maker.model.Tema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {

}
