
package br.com.reelbook.model;

public class RegistroPessoalDTO {

    private Filme filme;
    private RegistroPessoal registro;

    public RegistroPessoalDTO(Filme filme, RegistroPessoal registro) {
        this.filme = filme;
        this.registro = registro;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public RegistroPessoal getRegistro() {
        return registro;
    }

    public void setRegistro(RegistroPessoal registro) {
        this.registro = registro;
    }
}
