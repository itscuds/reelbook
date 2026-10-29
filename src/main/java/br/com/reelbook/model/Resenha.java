package br.com.reelbook.model;

import java.time.LocalDate;

public class Resenha {

    private int idResenha;
    private int idUsuario;
    private int idFilme;
    private String texto;
    private LocalDate dataPublicacao = LocalDate.now();

    private String nomeUsuario;
    private String tituloFilme;
    private String posterFilme;
    private int anoFilme;

    public Resenha() {
    }

    public int getIdResenha() {
        return idResenha;
    }

    public void setIdResenha(int idResenha) {
        this.idResenha = idResenha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdFilme() {
        return idFilme;
    }

    public void setIdFilme(int idFilme) {
        this.idFilme = idFilme;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getTituloFilme() {
        return tituloFilme;
    }

    public void setTituloFilme(String tituloFilme) {
        this.tituloFilme = tituloFilme;
    }

    public String getPosterFilme() {
        return posterFilme;
    }

    public void setPosterFilme(String posterFilme) {
        this.posterFilme = posterFilme;
    }

    public int getAnoFilme() {
        return anoFilme;
    }

    public void setAnoFilme(int anoFilme) {
        this.anoFilme = anoFilme;
    }
}