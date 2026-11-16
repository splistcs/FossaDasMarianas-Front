package fdmfront.solicitarcadastro.service;

import fdmfront.cadastro.model.Cadastro;
import fdmfront.config.ApiClient;
import fdmfront.config.ApiException;
import fdmfront.solicitarcadastro.model.SolicitacaoCadastro;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class SolicitarCadastroService {

    private static final String CAMINHO = "/solicitacoes-cadastro";

    @Inject
    private ApiClient apiClient;

    public Cadastro solicitar(SolicitacaoCadastro solicitacao) throws ApiException {
        return apiClient.criar(CAMINHO, solicitacao, Cadastro.class);
    }
}
