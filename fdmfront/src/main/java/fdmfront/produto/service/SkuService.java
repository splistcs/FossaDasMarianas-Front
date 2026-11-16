package fdmfront.produto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fdmfront.config.ApiException;
import fdmfront.produto.model.Produto;
import fdmfront.produto.model.Sku;
import fdmfront.produto.model.SkuProduto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Gerencia SKUs sem endpoint próprio: cada operação carrega o produto,
 * altera a lista de SKUs e grava o produto de volta (PUT /fdm/produtos/{id}).
 */
@ApplicationScoped
public class SkuService {

    @Inject
    private ProdutoService produtoService;

    /** Lista todos os SKUs de todos os produtos. */
    public List<SkuProduto> getAll() throws ApiException {
        List<SkuProduto> lista = new ArrayList<>();
        for (Produto produto : produtoService.getAll()) {
            for (Sku sku : produto.getSkus()) {
                lista.add(new SkuProduto(produto.getId(), produto.getNome(), sku));
            }
        }
        return lista;
    }

    /** Adiciona o SKU ao produto (sem id) ou substitui o existente (com id). */
    public void save(Long produtoId, Sku sku) throws ApiException {
        Produto produto = carregar(produtoId);
        if (sku.getId() == null) {
            produto.getSkus().add(sku);
        } else {
            int posicao = posicao(produto, sku.getId());
            if (posicao < 0) {
                throw new ApiException(404, "SKU não encontrado no produto.");
            }
            produto.getSkus().set(posicao, sku);
        }
        produtoService.save(produto);
    }

    public void deleteById(Long produtoId, Long skuId) throws ApiException {
        Produto produto = carregar(produtoId);
        if (!produto.getSkus().removeIf(sku -> Objects.equals(sku.getId(), skuId))) {
            throw new ApiException(404, "SKU não encontrado no produto.");
        }
        produtoService.save(produto);
    }

    private Produto carregar(Long produtoId) throws ApiException {
        return produtoService.findById(produtoId)
                .orElseThrow(() -> new ApiException(404, "Produto não encontrado."));
    }

    private static int posicao(Produto produto, Long skuId) {
        for (int i = 0; i < produto.getSkus().size(); i++) {
            if (Objects.equals(produto.getSkus().get(i).getId(), skuId)) {
                return i;
            }
        }
        return -1;
    }
}
