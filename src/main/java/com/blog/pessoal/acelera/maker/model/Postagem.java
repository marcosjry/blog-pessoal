package com.blog.pessoal.acelera.maker.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Postagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String texto;
    private Date data;

    @ManyToOne
    private Tema tema;

    @ManyToOne
    private Usuario usuario;

    public Usuario getUserId() {
        return usuario;
    }

    public void setUserId(Usuario user) {
        this.usuario = user;
    }

    public Tema getTemaId() {
        return tema;
    }

    public void setTemaId(Tema tema) {
        this.tema = tema;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
