package fdmfront.produto.controller;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import fdmfront.config.ApiException;
import fdmfront.fazerlogin.controller.SessaoCtrl;
import fdmfront.produto.model.Produto;
import fdmfront.produto.model.Sku;
import fdmfront.produto.service.ProdutoService;
import fdmfront.realizarpedido.service.RealizarPedidoService;
import fdmfront.util.Mensagens;
import fdmfront.util.Parametros;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class ProdutoDetalheCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ProdutoDetalheCtrl.class.getName());

    @Inject
    private ProdutoService produtoService;

    @Inject
    private RealizarPedidoService realizarPedidoService;

    @Inject
    private SessaoCtrl sessaoCtrl;

    private Long id;
    private Produto produto;
    private Long skuId;
    private int quantidade = 1;
    private String motivo = "Nenhum produto foi informado.";

    // pesquisa o produto pelo url, legal né.
    @PostConstruct
    public void iniciar() {
        String parametro = Parametros.obter("id");
        if (parametro == null || !parametro.matches("\\d+")) {
            LOG.log(Level.WARNING, "Detalhe do produto sem id válido: {0}", parametro);
            return;
        }
        id = Long.valueOf(parametro);
        carregar();
    }

    public void carregar() {
        try {
            Optional<Produto> encontrado = produtoService.findById(id);
            if (encontrado.isEmpty()) {
                motivo = "A API não encontrou o produto " + id + ".";
            } else if (!Boolean.TRUE.equals(encontrado.get().getAtivo())) {
                motivo = "Este produto está indisponível.";
            } else {
                produto = encontrado.get();
                if (!produto.getSkus().isEmpty()) {
                    skuId = produto.getSkus().get(0).getId();
                }
            }
        } catch (ApiException e) {
            motivo = "Falha ao carregar o produto: " + e.getMessage();
            Mensagens.erro("Falha ao carregar o produto", e.getMessage());
        }
        LOG.log(Level.INFO, "Detalhe do produto {0}: {1}", new Object[] { id, produto == null ? motivo : "ok" });
    }

    public void adicionarAoCarrinho() {
        if (!sessaoCtrl.isLogado()) {
            Mensagens.aviso("Entre para comprar", "Faça login para adicionar produtos ao carrinho.");
            return;
        }
        Sku sku = getSkuSelecionado();
        if (sku == null) {
            Mensagens.aviso("Escolha uma opção", "Selecione o tamanho/variação desejada.");
            return;
        }
        if (quantidade < 1 || quantidade > getEstoqueSelecionado()) {
            Mensagens.aviso("Quantidade inválida", "Disponível: " + getEstoqueSelecionado() + " unidade(s).");
            return;
        }
        try {
            realizarPedidoService.poeCarrinhoItem(sessaoCtrl.getId(), sku.getId(), quantidade);
            Mensagens.info("Adicionado ao carrinho", produto.getNome());
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível adicionar ao carrinho", e.getMessage());
        }
    }

    public Sku getSkuSelecionado() {
        if (produto == null) {
            return null;
        }
        return produto.getSkus().stream()
                .filter(sku -> Objects.equals(sku.getId(), skuId))
                .findFirst()
                .orElse(null);
    }

    // controle de estoque do sku, se não retornar nada, deixa em 0.
    public int getEstoqueSelecionado() {
        Sku sku = getSkuSelecionado();
        return sku == null || sku.getEstoque() == null ? 0 : sku.getEstoque();
    }

    // pega a imagem do produto,e troca pra do sku se tiver.
    public String getImagemExibida() {
        Sku sku = getSkuSelecionado();
        if (sku != null && sku.getImagemUrl() != null) {
            return sku.getImagemUrl();
        }
        return produto == null ? null : produto.getImagemPrincipalUrl();
    }

    // debug, se der algum erro, manda o motivo do erro. Precisei usar pra entender um bug que tava acontecendo. Vou deixar pq é bom.
    public String getMotivo() {
        return motivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
        this.quantidade = 1;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
