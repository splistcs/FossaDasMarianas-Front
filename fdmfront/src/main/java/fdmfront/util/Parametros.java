package fdmfront.util;

import jakarta.faces.context.FacesContext;


public final class Parametros {

    private Parametros() {
    }

    // pegar algum parametro, ou retornar null se n achar.
    public static String obter(String nome) {
        String valor = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(nome);
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
