package com.blog.pessoal.acelera.maker.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Tema {

    public Tema() {}

    public Tema(Long id, String descricao) {
        setId(id);
        setDescricao(descricao);
    }

    public Tema(String descricao) {
        setDescricao(descricao);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,  unique = true)
    private String descricao;

    @OneToMany(mappedBy = "tema", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Postagem> postagem;

    @ManyToOne
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Postagem> getPostagem() {
        return postagem;
    }

    public void setPostagem(List<Postagem> postagem) {
        this.postagem = postagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
