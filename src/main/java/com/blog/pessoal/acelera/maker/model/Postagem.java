package com.blog.pessoal.acelera.maker.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Postagem {

    public Postagem() {}

    public Postagem(String titulo, String texto, Tema tema, Usuario usuario) {
        setTitulo(titulo);
        setTexto(texto);
        setUserId(usuario);
        setTema(tema);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String texto;
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "tema_id", nullable = false)
    private Tema tema;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Usuario getUserId() {
        return usuario;
    }

    public void setUserId(Usuario user) {
        this.usuario = user;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
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
