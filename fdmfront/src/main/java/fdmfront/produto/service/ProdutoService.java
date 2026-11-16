package fdmfront.produto.service;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;

import fdmfront.config.ApiClient;
import fdmfront.config.ApiException;
import fdmfront.produto.model.Produto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class ProdutoService {

    private static final String CAMINHO = "/produtos";

    @Inject
    private ApiClient apiClient;

    public List<Produto> getAll() throws ApiException {
        return apiClient.obter(CAMINHO, new TypeReference<List<Produto>>() { });
    }

    // lista dos ativos.
    public List<Produto> getAllAtivos(Long categoriaId) throws ApiException {
        return getAll().stream()
                .filter(produto -> Boolean.TRUE.equals(produto.getAtivo()))
                .filter(produto -> categoriaId == null
                        || (produto.getCategoria() != null
                                && categoriaId.equals(produto.getCategoria().getId())))
                .toList();
    }

    public Optional<Produto> findById(Long id) throws ApiException {
        return apiClient.obterOpcional(CAMINHO + "/" + id, Produto.class);
    }

    // Manda um POST quando não tem id e atualiza PUT quando tem
    public Produto save(Produto produto) throws ApiException {
        try {
            if (produto.getId() == null) {
                return apiClient.criar(CAMINHO, produto, Produto.class);
            }
            apiClient.atualizar(CAMINHO + "/" + produto.getId(), produto);
            return produto;
        } catch (ApiException e) {
            if (e.getStatus() == 400 || e.getStatus() == 500) {
                throw new ApiException(e.getStatus(), "A API recusou o produto. Confira se o código "
                        + "universal de cada SKU já não existe e se os campos estão preenchidos "
                        + "(detalhe no console da API).", e);
            }
            throw e;
        }
    }

    public void deleteById(Long id) throws ApiException {
        apiClient.excluir(CAMINHO + "/" + id);
    }
}
