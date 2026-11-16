package fdmfront.produto.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.primefaces.PrimeFaces;

import fdmfront.categoria.model.Categoria;
import fdmfront.categoria.service.CategoriaService;
import fdmfront.config.ApiException;
import fdmfront.produto.model.Produto;
import fdmfront.produto.model.Sku;
import fdmfront.produto.service.ProdutoService;
import fdmfront.util.Mensagens;
import fdmfront.util.Parametros;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProdutoCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ProdutoCtrl.class.getName());

    @Inject
    private ProdutoService produtoService;

    @Inject
    private CategoriaService categoriaService;

    private List<Produto> produtos = new ArrayList<>();
    private List<Categoria> categorias = new ArrayList<>();
    private Produto produto = new Produto();
    private Long categoriaId;

    private String acao;
    private boolean exibirDialogo;

    @PostConstruct
    public void iniciar() {
        try {
            categorias = categoriaService.getAll();
        } catch (ApiException e) {
            Mensagens.erro("Falha ao carregar categorias", e.getMessage());
        }
        carregar();
        acao = Parametros.obter("acao");
        abrirPorAcao();
    }

    public void carregar() {
        try {
            produtos = produtoService.getAll();
        } catch (ApiException e) {
            produtos = new ArrayList<>();
            Mensagens.erro("Falha ao carregar produtos", e.getMessage());
        }
    }

    public void novo() {
        produto = new Produto();
        produto.getSkus().add(new Sku());
        categoriaId = null;
    }

    public void editar(Produto selecionado) {
        try {
            Optional<Produto> atual = produtoService.findById(selecionado.getId());
            if (atual.isEmpty()) {
                Mensagens.aviso("Produto não encontrado", "Ele pode ter sido removido.");
                carregar();
                return;
            }
            produto = atual.get();
            categoriaId = produto.getCategoria() == null ? null : produto.getCategoria().getId();
            PrimeFaces.current().executeScript("PF('dlgProduto').show()");
        } catch (ApiException e) {
            Mensagens.erro("Falha ao abrir o produto", e.getMessage());
        }
    }

    public void adicionarSku() {
        produto.getSkus().add(new Sku());
    }

    public void removerSku(Sku sku) {
        produto.getSkus().remove(sku);
    }

    public void salvar() {
        LOG.log(Level.INFO, "salvar(): nome={0}, skus={1}, categoriaId={2}",
                new Object[] { produto.getNome(), produto.getSkus().size(), categoriaId });
        if (!validar()) {
            LOG.warning("salvar(): validação do formulário falhou; nada foi enviado à API.");
            return;
        }
        produto.setCategoria(categorias.stream()
                .filter(c -> c.getId().equals(categoriaId))
                .findFirst()
                .orElse(null));
        try {
            produtoService.save(produto);
            Mensagens.info("Produto salvo", produto.getNome());
            exibirDialogo = false;
            PrimeFaces.current().executeScript("PF('dlgProduto').hide()");
            carregar();
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível salvar o produto", e.getMessage());
        }
    }

    public void excluir(Produto selecionado) {
        try {
            produtoService.deleteById(selecionado.getId());
            Mensagens.info("Produto excluído", selecionado.getNome());
            carregar();
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível excluir o produto", e.getMessage());
        }
    }

    private boolean validar() {
        boolean valido = true;
        if (vazio(produto.getNome()) || vazio(produto.getDescricao())
                || vazio(produto.getMaterial()) || vazio(produto.getMarca())) {
            Mensagens.aviso("Dados incompletos", "Informe nome, descrição, material e marca.");
            valido = false;
        }
        if (produto.getSkus().isEmpty()) {
            Mensagens.aviso("SKU obrigatório", "Cadastre ao menos um SKU para o produto.");
            return false;
        }
        for (int i = 0; i < produto.getSkus().size(); i++) {
            Sku sku = produto.getSkus().get(i);
            if (vazio(sku.getCodigoUniversal()) || sku.getEstoque() == null || sku.getPreco() == null
                    || sku.getPesoGramas() == null || sku.getAlturaCm() == null
                    || sku.getLarguraCm() == null || sku.getComprimentoCm() == null) {
                Mensagens.aviso("SKU incompleto", "Preencha todos os campos do SKU " + (i + 1) + ".");
                valido = false;
            }
        }
        return valido;
    }

    private static boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public Produto getProduto() {
        return produto;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
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
