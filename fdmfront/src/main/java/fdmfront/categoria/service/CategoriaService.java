package fdmfront.categoria.service;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;

import fdmfront.categoria.model.Categoria;
import fdmfront.config.ApiClient;
import fdmfront.config.ApiException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class CategoriaService {

    private static final String CAMINHO = "/categorias";

    @Inject
    private ApiClient apiClient;

    public List<Categoria> getAll() throws ApiException {
        return apiClient.obter(CAMINHO, new TypeReference<List<Categoria>>() { });
    }

    public List<Categoria> getAllAtivas() throws ApiException {
        return getAll().stream()
                .filter(categoria -> Boolean.TRUE.equals(categoria.getAtivo()))
                .toList();
    }

    public Optional<Categoria> findById(Long id) throws ApiException {
        return apiClient.obterOpcional(CAMINHO + "/" + id, Categoria.class);
    }

    // Manda um POST quando não tem id e atualiza PUT quando tem
    public Categoria save(Categoria categoria) throws ApiException {
        if (categoria.getId() == null) {
            return apiClient.criar(CAMINHO, categoria, Categoria.class);
        }
        apiClient.atualizar(CAMINHO + "/" + categoria.getId(), categoria);
        return categoria;
    }

    public void deleteById(Long id) throws ApiException {
        apiClient.excluir(CAMINHO + "/" + id);
    }
}
