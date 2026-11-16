package fdmfront.cadastro.service;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;

import fdmfront.cadastro.model.Cadastro;
import fdmfront.config.ApiClient;
import fdmfront.config.ApiException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class CadastroService {

    private static final String CAMINHO = "/cadastros";

    @Inject
    private ApiClient apiClient;

    public List<Cadastro> getAll() throws ApiException {
        return apiClient.obter(CAMINHO, new TypeReference<List<Cadastro>>() { });
    }

    public Optional<Cadastro> findById(Long id) throws ApiException {
        return apiClient.obterOpcional(CAMINHO + "/" + id, Cadastro.class);
    }

    // Manda um POST quando não tem id e atualiza PUT quando tem
    public Cadastro save(Cadastro cadastro) throws ApiException {
        if (cadastro.getId() == null) {
            return apiClient.criar(CAMINHO, cadastro, Cadastro.class);
        }
        apiClient.atualizar(CAMINHO + "/" + cadastro.getId(), cadastro);
        return cadastro;
    }

    public void deleteById(Long id) throws ApiException {
        apiClient.excluir(CAMINHO + "/" + id);
    }
}
