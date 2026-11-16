package fdmfront.cadastro.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.primefaces.PrimeFaces;

import fdmfront.cadastro.model.Cadastro;
import fdmfront.cadastro.service.CadastroService;
import fdmfront.config.ApiException;
import fdmfront.util.Mensagens;
import fdmfront.util.Parametros;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CadastroCtrl implements Serializable {

    private static final long serialVersionUID = 1L;

    //a api exige o campo, mas não o usa ao atualizar, ignorada n° 1.
    private static final String SENHA_IGNORADA_NA_EDICAO = "nao-alterada";

    @Inject
    private CadastroService cadastroService;

    private List<Cadastro> cadastros = new ArrayList<>();
    private Cadastro cadastro = new Cadastro();

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
            cadastros = cadastroService.getAll();
        } catch (ApiException e) {
            cadastros = new ArrayList<>();
            Mensagens.erro("Falha ao carregar cadastros", e.getMessage());
        }
    }

    public void novo() {
        cadastro = new Cadastro();
    }

    public void editar(Cadastro selecionado) {
        cadastro = new Cadastro();
        cadastro.setId(selecionado.getId());
        cadastro.setNomeCompleto(selecionado.getNomeCompleto());
        cadastro.setEmail(selecionado.getEmail());
        cadastro.setCpf(selecionado.getCpf());
        cadastro.setTelefone(selecionado.getTelefone());
        cadastro.setAtivo(selecionado.getAtivo());
        cadastro.setSenha(selecionado.getSenha());
        cadastro.setTipoCadastro(selecionado.getTipoCadastro());
    }

    public void salvar() {
        if (cadastro.getId() != null) {
            // O PUT da api exige uma "senha" no corpo, so que ignora hehe, ignorada n° 2.
            cadastro.setSenha(SENHA_IGNORADA_NA_EDICAO);
        }
        cadastro.setEmail(cadastro.getEmail().trim().toLowerCase(Locale.ROOT));
        cadastro.setCpf(somenteDigitos(cadastro.getCpf()));
        cadastro.setTelefone(somenteDigitos(cadastro.getTelefone()));
        try {
            cadastroService.save(cadastro);
            Mensagens.info("Cadastro salvo", cadastro.getNomeCompleto());
            exibirDialogo = false;
            PrimeFaces.current().executeScript("PF('dlgCadastro').hide()");
            carregar();
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível salvar o cadastro", e.getMessage());
        }
    }

    public void excluir(Cadastro selecionado) {
        try {
            cadastroService.deleteById(selecionado.getId());
            Mensagens.info("Cadastro excluído", selecionado.getNomeCompleto());
            carregar();
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível excluir o cadastro", e.getMessage());
        }
    }

    // a api guarda CPF e telefone só com dígitos. 
    private static String somenteDigitos(String valor) {
        return valor.replaceAll("\\D", "");
    }

    public Cadastro.TipoCadastro[] getTipos() {
        return Cadastro.TipoCadastro.values();
    }

    public List<Cadastro> getCadastros() {
        return cadastros;
    }

    public Cadastro getCadastro() {
        return cadastro;
    }

    public void setCadastro(Cadastro cadastro) {
        this.cadastro = cadastro;
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
