package fdmfront.validarsolicitacao.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fdmfront.cadastro.model.Cadastro;
import fdmfront.config.ApiException;
import fdmfront.util.Mensagens;
import fdmfront.validarsolicitacao.service.ValidarSolicitacaoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class ValidarSolicitacaoCtrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ValidarSolicitacaoService validarSolicitacaoService;

    private List<Cadastro> pendentes = new ArrayList<>();

    @PostConstruct
    public void iniciar() {
        carregar();
    }

    public void carregar() {
        try {
            pendentes = validarSolicitacaoService.listarPendentes();
        } catch (ApiException e) {
            pendentes = new ArrayList<>();
            Mensagens.erro("Falha ao carregar solicitações", e.getMessage());
        }
    }

    public void aprovar(Cadastro solicitacao) {
        try {
            validarSolicitacaoService.aprovar(solicitacao.getId());
            Mensagens.info("Solicitação aprovada", solicitacao.getNomeCompleto());
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível aprovar", e.getMessage());
        }
        carregar();
    }

    public void recusar(Cadastro solicitacao) {
        try {
            validarSolicitacaoService.recusar(solicitacao.getId());
            Mensagens.info("Solicitação recusada", solicitacao.getNomeCompleto());
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível recusar", e.getMessage());
        }
        carregar();
    }

    public List<Cadastro> getPendentes() {
        return pendentes;
    }
}
