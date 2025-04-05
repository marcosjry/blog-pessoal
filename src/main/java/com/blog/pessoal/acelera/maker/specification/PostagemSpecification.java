package com.blog.pessoal.acelera.maker.specification;

import com.blog.pessoal.acelera.maker.model.Postagem;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class PostagemSpecification {
    public static Specification<Postagem> filtrar(Long autorId, Long temaId) {
        return (Root<Postagem> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction(); // Sempre começa com um TRUE

            if (autorId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("usuario").get("id"), autorId));
            }
            if (temaId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("tema").get("id"), temaId));
            }

            return predicate;
        };
    }
}
