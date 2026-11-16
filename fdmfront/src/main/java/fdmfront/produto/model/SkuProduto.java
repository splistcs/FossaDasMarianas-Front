package fdmfront.produto.model;

import java.io.Serializable;

// fiz esse pra juntar o produto e o sku, preciso pra fazer algumas funções legais no front.
public class SkuProduto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long produtoId;
    private final String produtoNome;
    private final Sku sku;

    public SkuProduto(Long produtoId, String produtoNome, Sku sku) {
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.sku = sku;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public Sku getSku() {
        return sku;
    }
}
