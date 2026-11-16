package fdmfront.produto.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fdmfront.categoria.model.Categoria;


public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String descricao;
    private String material;
    private String marca;
    private Boolean ativo = Boolean.TRUE;
    private String imagemPrincipalUrl;
    private List<Sku> skus = new ArrayList<>();
    private Categoria categoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getImagemPrincipalUrl() {
        return imagemPrincipalUrl;
    }

    public void setImagemPrincipalUrl(String imagemPrincipalUrl) {
        this.imagemPrincipalUrl = imagemPrincipalUrl;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus == null ? new ArrayList<>() : new ArrayList<>(skus);
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // menor preço sempre, queremos vender né senhores.
    @JsonIgnore
    public BigDecimal getPrecoInicial() {
        return skus.stream()
                .map(Sku::getPreco)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    // estoque total do produto, somando os skus.
    @JsonIgnore
    public int getEstoqueTotal() {
        return skus.stream()
                .map(Sku::getEstoque)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
