package fdmfront.solicitarcadastro.controller;

import java.io.Serializable;
import java.util.List;

import fdmfront.config.ApiException;
import fdmfront.endereco.model.Endereco;
import fdmfront.solicitarcadastro.model.SolicitacaoCadastro;
import fdmfront.solicitarcadastro.service.SolicitarCadastroService;
import fdmfront.util.Mensagens;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class SolicitarCadastroCtrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private SolicitarCadastroService solicitarCadastroService;

    private SolicitacaoCadastro solicitacao = new SolicitacaoCadastro();
    private Endereco endereco = new Endereco();
    private String confirmacaoSenha;

    public void solicitar() {
        if (!solicitacao.getSenha().equals(confirmacaoSenha)) {
            Mensagens.aviso("Senhas diferentes", "A confirmação deve ser igual à senha.");
            return;
        }
        solicitacao.setEnderecos(List.of(endereco));
        try {
            solicitarCadastroService.solicitar(solicitacao);
            Mensagens.info("Solicitação enviada", "Aguarde a aprovação do gestor para acessar sua conta.");
            solicitacao = new SolicitacaoCadastro();
            endereco = new Endereco();
            confirmacaoSenha = null;
        } catch (ApiException e) {
            Mensagens.erro("Não foi possível enviar a solicitação", e.getMessage());
        }
    }

    public SolicitacaoCadastro getSolicitacao() {
        return solicitacao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }
}
