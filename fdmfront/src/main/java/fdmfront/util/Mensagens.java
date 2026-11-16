package fdmfront.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

// mensagenzinhas pré programadas.
public final class Mensagens {

    private Mensagens() {
    }

    public static void info(String resumo, String detalhe) {
        adicionar(FacesMessage.SEVERITY_INFO, resumo, detalhe);
    }

    public static void aviso(String resumo, String detalhe) {
        adicionar(FacesMessage.SEVERITY_WARN, resumo, detalhe);
    }

    public static void erro(String resumo, String detalhe) {
        adicionar(FacesMessage.SEVERITY_ERROR, resumo, detalhe);
    }

    private static void adicionar(FacesMessage.Severity severidade, String resumo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidade, resumo, detalhe));
    }
}
