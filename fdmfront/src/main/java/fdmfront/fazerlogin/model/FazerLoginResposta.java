package fdmfront.fazerlogin.model;

import java.io.Serializable;

// modelo pro sisteminha de resposta do login.
public class FazerLoginResposta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nomeCompleto;
    private String email;
    private String tipoCadastro;
    private String token;
    private String tipoToken;

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

    public String getTipoCadastro() {
        return tipoCadastro;
    }

    public void setTipoCadastro(String tipoCadastro) {
        this.tipoCadastro = tipoCadastro;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }
}
