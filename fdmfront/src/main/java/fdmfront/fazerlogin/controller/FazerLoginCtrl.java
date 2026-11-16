package fdmfront.fazerlogin.controller;

import java.io.Serializable;

import fdmfront.config.ApiException;
import fdmfront.fazerlogin.model.FazerLoginResposta;
import fdmfront.fazerlogin.service.FazerLoginService;
import fdmfront.util.Mensagens;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class FazerLoginCtrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FazerLoginService fazerLoginService;

    @Inject
    private SessaoCtrl sessaoCtrl;

    private String email;
    private String senha;

    // ele manda pra tela principal se der certo, senão da erro.
    public String entrar() {
        try {
            FazerLoginResposta resposta = fazerLoginService.login(email, senha);
            sessaoCtrl.entrar(resposta);
            return "/index?faces-redirect=true";
        } catch (ApiException e) {
            Mensagens.aviso("Não foi possível entrar", e.getMessage());
            return null;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
