package fdmfront.solicitarcadastro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fdmfront.endereco.model.Endereco;

public class SolicitacaoCadastro implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nomeCompleto;
    private String email;
    private String cpf;
    private String telefone;
    private String senha;
    private List<Endereco> enderecos = new ArrayList<>();

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos == null ? new ArrayList<>() : enderecos;
    }
}
