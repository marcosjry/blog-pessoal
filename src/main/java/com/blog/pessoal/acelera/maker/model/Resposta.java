package com.blog.pessoal.acelera.maker.model;

public class Resposta {

    private String mensagem;
    private String status;

    public Resposta(String mensagem, String status) {
        setMensagem(mensagem);
        setStatus(status);
    }

    public Resposta(){};

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
