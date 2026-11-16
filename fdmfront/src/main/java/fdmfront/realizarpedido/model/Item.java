package fdmfront.realizarpedido.model;

import java.io.Serializable;
import java.math.BigDecimal;

import fdmfront.produto.model.Sku;


public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer quantidade;
    private BigDecimal precoFinal;
    private Sku skuDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoFinal() {
        return precoFinal;
    }

    public void setPrecoFinal(BigDecimal precoFinal) {
        this.precoFinal = precoFinal;
    }

    public Sku getSkuDto() {
        return skuDto;
    }

    public void setSkuDto(Sku skuDto) {
        this.skuDto = skuDto;
    }
}
