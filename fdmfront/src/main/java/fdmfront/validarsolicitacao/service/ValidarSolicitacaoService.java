package fdmfront.validarsolicitacao.service;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import fdmfront.cadastro.model.Cadastro;
import fdmfront.config.ApiClient;
import fdmfront.config.ApiException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class ValidarSolicitacaoService {

    private static final String CAMINHO = "/gestor/solicitacoes";

    @Inject
    private ApiClient apiClient;

    public List<Cadastro> listarPendentes() throws ApiException {
        return apiClient.obter(CAMINHO, new TypeReference<List<Cadastro>>() { });
    }

    // aprocar o cadastro
    public Cadastro aprovar(Long id) throws ApiException {
        return apiClient.postar(CAMINHO + "/" + id + "/aprovar", Cadastro.class);
    }

    // apaga a solicitação.
    public void recusar(Long id) throws ApiException {
        apiClient.postar(CAMINHO + "/" + id + "/recusar");
    }
}
