package fdmfront.realizarpedido.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fdmfront.endereco.model.Endereco;


public class Pedido implements Serializable {

    public static final String CARRINHO = "CARRINHO";
    public static final String PENDENTE = "PENDENTE";
    public static final String PAGO = "PAGO";
    public static final String ENVIADO = "ENVIADO";
    public static final String ENTREGUE = "ENTREGUE";
    public static final String CANCELADO = "CANCELADO";

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Long id;
    private Object dataPedido;
    private BigDecimal valorTotal;
    private BigDecimal valorFrete;
    private Integer idSessao;
    private String statusPedido;
    private List<Item> itemPedido = new ArrayList<>();
    private Endereco enderecoDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Object dataPedido) {
        this.dataPedido = dataPedido;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public Integer getIdSessao() {
        return idSessao;
    }

    public void setIdSessao(Integer idSessao) {
        this.idSessao = idSessao;
    }

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }

    public List<Item> getItemPedido() {
        return itemPedido;
    }

    public void setItemPedido(List<Item> itemPedido) {
        this.itemPedido = itemPedido == null ? new ArrayList<>() : itemPedido;
    }

    public Endereco getEnderecoDto() {
        return enderecoDto;
    }

    public void setEnderecoDto(Endereco enderecoDto) {
        this.enderecoDto = enderecoDto;
    }

    @JsonIgnore
    public BigDecimal getTotalItens() {
        return itemPedido.stream()
                .map(Item::getPrecoFinal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @JsonIgnore
    public String getDataFormatada() {
        if (dataPedido == null) {
            return "";
        }
        try {
            return FORMATO.format(LocalDateTime.parse(dataPedido.toString()));
        } catch (DateTimeParseException e) {
            return dataPedido.toString();
        }
    }

    // rotulozinhos para cada status.
    @JsonIgnore
    public String getRotuloStatus() {
        if (statusPedido == null) {
            return "";
        }
        switch (statusPedido) {
            case CARRINHO:
                return "Carrinho";
            case PENDENTE:
                return "Aguardando pagamento";
            case PAGO:
                return "Pago";
            case ENVIADO:
                return "Enviado";
            case ENTREGUE:
                return "Entregue";
            case CANCELADO:
                return "Cancelado";
            default:
                return statusPedido;
        }
    }
}
