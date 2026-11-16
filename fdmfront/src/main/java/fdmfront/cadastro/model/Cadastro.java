package fdmfront.cadastro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import fdmfront.endereco.model.Endereco;


public class Cadastro implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum TipoCadastro {
        ADMIN,
        CLIENTE
    }

    private Long id;
    private String nomeCompleto;
    private String email;

    @JsonProperty("CPF")
    @JsonAlias("cpf")
    private String cpf;

    private String telefone;
    private Boolean ativo = Boolean.TRUE;
    private String senha;

    // Forço a ser ReadOnly, por segurança.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Endereco> enderecos = new ArrayList<>();

    @JsonProperty("TipoCadastro")
    @JsonAlias("tipoCadastro")
    private TipoCadastro tipoCadastro = TipoCadastro.CLIENTE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoCadastro getTipoCadastro() {
        return tipoCadastro;
    }

    public void setTipoCadastro(TipoCadastro tipoCadastro) {
        this.tipoCadastro = tipoCadastro;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos == null ? new ArrayList<>() : enderecos;
    }
}
