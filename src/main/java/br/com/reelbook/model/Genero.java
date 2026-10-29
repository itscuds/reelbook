package br.com.reelbook.model;

public class Genero {

    private int idGenero;
    private String nome;

    public Genero() {
    }

    public Genero(int idGenero, String nome) {
        this.idGenero = idGenero;
        this.nome = nome;
    }

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}