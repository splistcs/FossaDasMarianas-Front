package fdmfront.categoria.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;

import fdmfront.categoria.model.Categoria;
import fdmfront.categoria.service.CategoriaService;
import fdmfront.config.ApiException;
import fdmfront.util.Mensagens;
import fdmfront.util.Parametros;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class CategoriaCtrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CategoriaService categoriaService;

    private List<Categoria> categorias = new ArrayList<>();
    private Categoria categoria = new Categoria();

    private String acao;
    private boolean exibirDialogo;

    @PostConstruct
    public void iniciar() {
        carregar();
        acao = Parametros.obter("acao");
        abrirPorAcao();
    }

    public void carregar() {
        try {
            categorias = categoriaService.getAll();
        } catch (ApiException e) {
            categorias = new ArrayList<>();
            Mensagens.erro("Falha ao carregar categorias", e.getMessage());
        }
    }

    public void novo() {
        categoria = new Categoria();
    }

    public void editar(Categoria selecionada) {
        categoria = new Categoria();
        categoria.setId(selecionada.getId());
        categoria.setNome(selecionada.getNome());
        categoria.setAtivo(selecionada.getAtivo());
    }

    public void salvar() {
        try {
            categoriaService.save(categoria);
            Mensagens.info("Categoria salva", categoria.getNome());
            exibirDialogo = false;
            PrimeFaces.current().executeScript("PF('dlgCategoria').hide()");
            carregar();
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível salvar a categoria", e.getMessage());
        }
    }

    public void excluir(Categoria selecionada) {
        try {
            categoriaService.deleteById(selecionada.getId());
            Mensagens.info("Categoria excluída", selecionada.getNome());
            carregar();
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível excluir a categoria", e.getMessage());
        }
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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
