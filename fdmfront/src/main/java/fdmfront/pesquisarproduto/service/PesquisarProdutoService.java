package fdmfront.pesquisarproduto.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import fdmfront.config.ApiClient;
import fdmfront.config.ApiException;
import fdmfront.produto.model.Produto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

// Cho, vc tá de parabéns pelo sistema de pesquisa, fico cada dia mais impressionado pelas gambiarras que funcionam. <- digo gambiarras com respeito tá, achei 10.
@ApplicationScoped
public class PesquisarProdutoService {

    private static final String CAMINHO = "/pesquisarproduto";

    @Inject
    private ApiClient apiClient;

    /*
     o que vai ser pesquisado:
     
        nome        obrigatório
        material    opcional
        marca       opcional
        categoriaId opcional
        e tem que retornar uma lista de produtos, vazia quando a api apita 404
     */
    public List<Produto> findByFiltros(String nome, String material, String marca, Long categoriaId)
            throws ApiException {
        StringBuilder consulta = new StringBuilder(CAMINHO)
                .append("?nome=").append(codificar(nome));
        adicionar(consulta, "material", material);
        adicionar(consulta, "marca", marca);
        if (categoriaId != null) {
            consulta.append("&id=").append(categoriaId);
        }
        try {
            return apiClient.obter(consulta.toString(), new TypeReference<List<Produto>>() { });
        } catch (ApiException e) {
            if (e.getStatus() == 404) {
                return List.of();
            }
            throw e;
        }
    }

    private static void adicionar(StringBuilder consulta, String parametro, String valor) {
        if (valor != null && !valor.isBlank()) {
            consulta.append('&').append(parametro).append('=').append(codificar(valor));
        }
    }

    private static String codificar(String valor) {
        return URLEncoder.encode(valor.trim(), StandardCharsets.UTF_8);
    }
}
