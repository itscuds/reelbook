package br.com.reelbook.model;

import java.time.LocalDate;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senhaHash;
    private String perfil;
    private LocalDate dataCadastro;
    private boolean ativo;

    public Usuario() {}

    public Usuario(String trim, String trim1, String senha) {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public int getIdUsuario() {
        return id;
    }
}