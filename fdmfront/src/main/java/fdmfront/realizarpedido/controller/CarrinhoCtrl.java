package fdmfront.realizarpedido.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fdmfront.config.ApiException;
import fdmfront.fazerlogin.controller.SessaoCtrl;
import fdmfront.produto.model.Produto;
import fdmfront.produto.model.Sku;
import fdmfront.produto.service.ProdutoService;
import fdmfront.realizarpedido.model.Item;
import fdmfront.realizarpedido.model.Pedido;
import fdmfront.realizarpedido.service.RealizarPedidoService;
import fdmfront.util.Mensagens;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class CarrinhoCtrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RealizarPedidoService realizarPedidoService;

    @Inject
    private ProdutoService produtoService;

    @Inject
    private SessaoCtrl sessaoCtrl;

    private Pedido carrinho;

    // nome do produto do sku.
    private final Map<Long, String> nomesPorSku = new HashMap<>();

    @PostConstruct
    public void iniciar() {
        carregar();
    }

    public void carregar() {
        if (!sessaoCtrl.isLogado()) {
            carrinho = null;
            return;
        }
        try {
            carrinho = realizarPedidoService.getCarrinho(sessaoCtrl.getId());
            carregarNomes();
        } catch (ApiException e) {
            carrinho = null;
            Mensagens.erro("Falha ao carregar o carrinho", e.getMessage());
        }
    }

    // alterar a qte no carrinho
    public void alterarQuantidade(Item item) {
        try {
            Item atualizado = realizarPedidoService.alterarItemCarrinho(item.getId(), item.getQuantidade());
            item.setPrecoFinal(atualizado.getPrecoFinal());
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível alterar a quantidade", e.getMessage());
            carregar();
        }
    }

    public void remover(Item item) {
        try {
            realizarPedidoService.deletarItemPorId(item.getId());
            Mensagens.info("Item removido", nomeProduto(item));
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível remover o item", e.getMessage());
        }
        carregar();
    }

    // inicia o pagamento do mock.
    public void finalizar() {
        if (carrinho == null || carrinho.getItemPedido().isEmpty()) {
            Mensagens.aviso("Carrinho vazio", "Adicione produtos antes de finalizar.");
            return;
        }
        try {
            String urlPagamento = realizarPedidoService.iniciarPagamento(carrinho.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect(urlPagamento.trim());
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível finalizar a compra", e.getMessage());
            carregar();
        } catch (IOException e) {
            Mensagens.erro("Não foi possível abrir a página de pagamento", e.getMessage());
        }
    }

    public String nomeProduto(Item item) {
        Sku sku = item.getSkuDto();
        String nome = sku == null ? null : nomesPorSku.get(sku.getId());
        return nome == null ? "Produto" : nome;
    }

    public List<Item> getItens() {
        return carrinho == null ? List.of() : carrinho.getItemPedido();
    }

    public BigDecimal getTotal() {
        return carrinho == null ? BigDecimal.ZERO : carrinho.getTotalItens();
    }

    private void carregarNomes() throws ApiException {
        nomesPorSku.clear();
        for (Produto produto : produtoService.getAll()) {
            for (Sku sku : produto.getSkus()) {
                nomesPorSku.put(sku.getId(), produto.getNome());
            }
        }
    }
}
