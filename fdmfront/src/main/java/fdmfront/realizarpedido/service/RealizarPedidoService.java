package fdmfront.realizarpedido.service;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import fdmfront.config.ApiClient;
import fdmfront.config.ApiException;
import fdmfront.realizarpedido.model.Item;
import fdmfront.realizarpedido.model.Pedido;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class RealizarPedidoService {

    @Inject
    private ApiClient apiClient;

    // adicionar no carrinho
    public Item poeCarrinhoItem(Long cadastroId, Long skuId, int quant) throws ApiException {
        String caminho = "/item?cadastroId=" + cadastroId + "&skuId=" + skuId + "&quant=" + quant;
        try {
            return apiClient.postar(caminho, Item.class);
        } catch (ApiException e) {
            switch (e.getStatus()) {
                case 400:
                    throw new ApiException(400, "Quantidade inválida ou produto sem preço.", e);
                case 401:
                    throw new ApiException(401, "Seu cadastro não está ativo para comprar.", e);
                default:
                    throw e;
            }
        }
    }

    public Pedido getCarrinho(Long cadastroId) throws ApiException {
        return apiClient.postar("/pedido?cadastroId=" + cadastroId, Pedido.class);
    }

    public Item alterarItemCarrinho(Long itemId, int quant) throws ApiException {
        return apiClient.atualizarSemCorpo("/item/" + itemId + "?quant=" + quant, Item.class);
    }

    public void deletarItemPorId(Long itemId) throws ApiException {
        apiClient.excluir("/item/" + itemId);
    }

    public List<Pedido> getPedidosPorCadastro(Long cadastroId) throws ApiException {
        return apiClient.obter("/pedidos?cadastroId=" + cadastroId, new TypeReference<List<Pedido>>() { });
    }

    public String iniciarPagamento(Long pedidoId) throws ApiException {
        try {
            return apiClient.postarComTexto("/pedido/" + pedidoId + "/pagamento");
        } catch (ApiException e) {
            switch (e.getStatus()) {
                case 400:
                    throw new ApiException(400, "Só um carrinho com itens pode ser pago.", e);
                case 403:
                    throw new ApiException(403, "O serviço de pagamento recusou o pedido.", e);
                case 404:
                    throw new ApiException(404, "Pedido não encontrado.", e);
                case 500:
                    throw new ApiException(500, "Não foi possível iniciar o pagamento. "
                            + "O Mock (localhost:8000) está no ar?", e);
                default:
                    throw e;
            }
        }
    }

    // atualizar o status do pedido.
    public Pedido atualizarPedidoPorId(Long pedidoId, String proximoStatus) throws ApiException {
        return apiClient.atualizarSemCorpo("/pedido/" + pedidoId + "/status?status=" + proximoStatus, Pedido.class);
    }
}
