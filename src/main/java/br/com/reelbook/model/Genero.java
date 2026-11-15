package br.com.reelbook.model;

public class Genero {

    private int idGenero;
    private String nome;
    private String descricao;
    private Integer ordem;
    private boolean ativo = true;

    public Genero() {
    }


    public Genero(int idGenero, String nome) {
        this.idGenero = idGenero;
        this.nome = nome;
    }

    public int getIdGenero() { return idGenero; }
    public void setIdGenero(int idGenero) { this.idGenero = idGenero; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}