package fdmfront.pesquisarproduto.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fdmfront.categoria.model.Categoria;
import fdmfront.categoria.service.CategoriaService;
import fdmfront.config.ApiException;
import fdmfront.pesquisarproduto.service.PesquisarProdutoService;
import fdmfront.produto.model.Produto;
import fdmfront.util.Mensagens;
import fdmfront.util.Parametros;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class BuscaCtrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private PesquisarProdutoService pesquisarProdutoService;

    @Inject
    private CategoriaService categoriaService;

    private String nome;
    private String material;
    private String marca;
    private Long categoriaId;
    private List<Categoria> categorias = new ArrayList<>();
    private List<Produto> resultados = new ArrayList<>();
    private boolean pesquisou;

    @PostConstruct
    public void iniciar() {
        try {
            categorias = categoriaService.getAllAtivas();
        } catch (ApiException e) {
            Mensagens.erro("Falha ao carregar categorias", e.getMessage());
        }
        nome = Parametros.obter("nome");
        pesquisarSeInformado();
    }

    // pega pela busca do url também, achei genial a sacada hehe.
    public void pesquisarSeInformado() {
        if (nome != null && !nome.isBlank()) {
            pesquisar();
        }
    }

    public void pesquisar() {
        if (nome == null || nome.isBlank()) {
            Mensagens.aviso("Informe o nome", "O nome do produto é obrigatório para a busca.");
            return;
        }
        pesquisou = true;
        try {
            resultados = pesquisarProdutoService.findByFiltros(nome, material, marca, categoriaId);
        } catch (ApiException e) {
            resultados = new ArrayList<>();
            Mensagens.erro("Falha na busca", e.getMessage());
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public List<Produto> getResultados() {
        return resultados;
    }

    public boolean isPesquisou() {
        return pesquisou;
    }
}
