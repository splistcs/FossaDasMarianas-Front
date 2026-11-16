package fdmfront.produto.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;

import fdmfront.config.ApiException;
import fdmfront.produto.model.Produto;
import fdmfront.produto.model.Sku;
import fdmfront.produto.model.SkuProduto;
import fdmfront.produto.service.ProdutoService;
import fdmfront.produto.service.SkuService;
import fdmfront.util.Mensagens;
import fdmfront.util.Parametros;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class SkuCtrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private SkuService skuService;

    @Inject
    private ProdutoService produtoService;

    private List<SkuProduto> skus = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();
    private Sku sku = new Sku();
    private Long produtoId;
    private String acao;
    private boolean exibirDialogo;

    @PostConstruct
    public void iniciar() {
        try {
            produtos = produtoService.getAll();
        } catch (ApiException e) {
            Mensagens.erro("Falha ao carregar produtos", e.getMessage());
        }
        carregar();
        acao = Parametros.obter("acao");
        abrirPorAcao();
    }

    public void carregar() {
        try {
            skus = skuService.getAll();
        } catch (ApiException e) {
            skus = new ArrayList<>();
            Mensagens.erro("Falha ao carregar SKUs", e.getMessage());
        }
    }

    public void novo() {
        sku = new Sku();
        produtoId = null;
    }

    public void editar(SkuProduto selecionado) {
        Sku origem = selecionado.getSku();
        sku = new Sku();
        sku.setId(origem.getId());
        sku.setEstoque(origem.getEstoque());
        sku.setPreco(origem.getPreco());
        sku.setEspecificacoesTexto(origem.getEspecificacoesTexto());
        sku.setPesoGramas(origem.getPesoGramas());
        sku.setCodigoUniversal(origem.getCodigoUniversal());
        sku.setAlturaCm(origem.getAlturaCm());
        sku.setLarguraCm(origem.getLarguraCm());
        sku.setComprimentoCm(origem.getComprimentoCm());
        produtoId = selecionado.getProdutoId();
    }

    public void salvar() {
        if (!validar()) {
            return;
        }
        try {
            skuService.save(produtoId, sku);
            Mensagens.info("SKU salvo", sku.getCodigoUniversal());
            exibirDialogo = false;
            PrimeFaces.current().executeScript("PF('dlgSku').hide()");
            carregar();
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível salvar o SKU", e.getMessage());
        }
    }

    public void excluir(SkuProduto selecionado) {
        try {
            skuService.deleteById(selecionado.getProdutoId(), selecionado.getSku().getId());
            Mensagens.info("SKU excluído", selecionado.getSku().getCodigoUniversal());
            carregar();
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível excluir o SKU", e.getMessage());
        }
    }

    // Abre o diálogo de cadastro quando a URL traz acao=novo (pelo botaozinho flutuante).
    public void abrirPorAcao() {
        if ("novo".equals(acao)) {
            novo();
            exibirDialogo = true;
        }
    }

    public void fecharDialogo() {
        exibirDialogo = false;
    }

    // pra não deixar o usuário mudar o id na edição, pra evitar explosões desnecessárias.
    public boolean isEdicao() {
        return sku.getId() != null;
    }

    private boolean validar() {
        if (produtoId == null) {
            Mensagens.aviso("Produto obrigatório", "Escolha o produto do SKU.");
            return false;
        }
        if (sku.getCodigoUniversal() == null || sku.getCodigoUniversal().isBlank()
                || sku.getEstoque() == null || sku.getPreco() == null
                || sku.getPesoGramas() == null || sku.getAlturaCm() == null
                || sku.getLarguraCm() == null || sku.getComprimentoCm() == null) {
            Mensagens.aviso("SKU incompleto", "Preencha todos os campos do SKU.");
            return false;
        }
        return true;
    }

    public List<SkuProduto> getSkus() {
        return skus;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public Sku getSku() {
        return sku;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public boolean isExibirDialogo() {
        return exibirDialogo;
    }
}
