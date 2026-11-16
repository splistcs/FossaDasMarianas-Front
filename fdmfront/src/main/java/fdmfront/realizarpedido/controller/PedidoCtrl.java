package fdmfront.realizarpedido.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fdmfront.config.ApiException;
import fdmfront.fazerlogin.controller.SessaoCtrl;
import fdmfront.realizarpedido.model.Pedido;
import fdmfront.realizarpedido.service.RealizarPedidoService;
import fdmfront.util.Mensagens;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class PedidoCtrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RealizarPedidoService realizarPedidoService;

    @Inject
    private SessaoCtrl sessaoCtrl;

    private List<Pedido> pedidos = new ArrayList<>();

    @PostConstruct
    public void iniciar() {
        carregar();
    }

    public void carregar() {
        pedidos = new ArrayList<>();
        if (!sessaoCtrl.isLogado()) {
            return;
        }
        try {
            for (Pedido pedido : realizarPedidoService.getPedidosPorCadastro(sessaoCtrl.getId())) {
                if (!Pedido.CARRINHO.equals(pedido.getStatusPedido())) {
                    pedidos.add(pedido);
                }
            }
        } catch (ApiException e) {
            Mensagens.erro("Falha ao carregar os pedidos", e.getMessage());
        }
    }

    // so admins conseguem controlar se foi enviado, etc.
    public boolean podeAvancar(Pedido pedido) {
        return sessaoCtrl.isAdmin()
                && (Pedido.PAGO.equals(pedido.getStatusPedido()) || Pedido.ENVIADO.equals(pedido.getStatusPedido()));
    }

    public void avancar(Pedido pedido) {
        String proximo = Pedido.PAGO.equals(pedido.getStatusPedido()) ? Pedido.ENVIADO : Pedido.ENTREGUE;
        try {
            realizarPedidoService.atualizarPedidoPorId(pedido.getId(), proximo);
            Mensagens.info("Pedido atualizado", "Pedido " + pedido.getId());
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível atualizar o pedido", e.getMessage());
        }
        carregar();
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
