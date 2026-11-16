package fdmfront.fazerlogin.service;

import java.util.Map;

import fdmfront.config.ApiClient;
import fdmfront.config.ApiException;
import fdmfront.fazerlogin.model.FazerLoginResposta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class FazerLoginService {

    private static final String CAMINHO = "/login";

    @Inject
    private ApiClient apiClient;

    // sisteminha de login simples.
    public FazerLoginResposta login(String email, String senha) throws ApiException {
        Map<String, String> credenciais = Map.of("email", email.trim(), "senha", senha);
        try {
            return apiClient.criar(CAMINHO, credenciais, FazerLoginResposta.class);
        } catch (ApiException e) {
            switch (e.getStatus()) {
                case 400:
                    throw new ApiException(400, "Informe um e-mail e uma senha válidos.", e);
                case 401:
                    throw new ApiException(401, "E-mail ou senha incorretos.", e);
                case 403:
                    throw new ApiException(403, "Seu cadastro ainda não foi ativado pelo gestor.", e);
                default:
                    throw e;
            }
        }
    }
}
