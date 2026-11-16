package fdmfront.fazerlogin.controller;

import java.io.Serializable;

import fdmfront.config.TokenSessao;
import fdmfront.fazerlogin.model.FazerLoginResposta;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;


@Named
@SessionScoped
public class SessaoCtrl implements Serializable {

    // atributos de admin.
    public static final String ATRIBUTO_ADMIN = "fdm.admin";

    private static final String TIPO_ADMIN = "ADMIN";
    private static final long serialVersionUID = 1L;

    @Inject
    private TokenSessao tokenSessao;

    private Long id;
    private String nomeUsuario;
    private String tipoCadastro;

    // sisteminha de token igual ao do bipe que fiz, parecido no caso.
    public void entrar(FazerLoginResposta resposta) {
        id = resposta.getId();
        nomeUsuario = resposta.getNomeCompleto();
        tipoCadastro = resposta.getTipoCadastro();
        tokenSessao.setToken(resposta.getToken());

        ExternalContext externo = FacesContext.getCurrentInstance().getExternalContext();
        ((HttpServletRequest) externo.getRequest()).changeSessionId();
        if (isAdmin()) {
            externo.getSessionMap().put(ATRIBUTO_ADMIN, Boolean.TRUE);
        }
    }

    public String sair() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index?faces-redirect=true";
    }

    public boolean isLogado() {
        return id != null;
    }

    public boolean isAdmin() {
        return TIPO_ADMIN.equals(tipoCadastro);
    }

    public Long getId() {
        return id;
    }

    public String getNomeUsuario() {
        return nomeUsuario == null ? "" : nomeUsuario;
    }
}
