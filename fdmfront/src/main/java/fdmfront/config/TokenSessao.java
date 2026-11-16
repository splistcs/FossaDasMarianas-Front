package fdmfront.config;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;

// Sisteminha de tokens de sessão.
@SessionScoped
public class TokenSessao implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
