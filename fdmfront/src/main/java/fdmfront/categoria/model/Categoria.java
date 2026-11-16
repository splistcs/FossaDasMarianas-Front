package fdmfront.categoria.model;

import java.io.Serializable;


public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private Boolean ativo = Boolean.TRUE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
