package fdmfront.produto.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fdmfront.categoria.model.Categoria;
import fdmfront.categoria.service.CategoriaService;
import fdmfront.config.ApiException;
import fdmfront.produto.model.Produto;
import fdmfront.produto.service.ProdutoService;
import fdmfront.util.Mensagens;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class VitrineCtrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ProdutoService produtoService;

    @Inject
    private CategoriaService categoriaService;

    private List<Categoria> categorias = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();
    private Long categoriaId;

    @PostConstruct
    public void iniciar() {
        try {
            categorias = categoriaService.getAllAtivas();
        } catch (ApiException e) {
            Mensagens.erro("Falha ao carregar categorias", e.getMessage());
        }
        filtrar();
    }

    public void filtrar() {
        try {
            produtos = produtoService.getAllAtivos(categoriaId);
        } catch (ApiException e) {
            produtos = new ArrayList<>();
            Mensagens.erro("Falha ao carregar produtos", e.getMessage());
        }
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
