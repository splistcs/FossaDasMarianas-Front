package fdmfront.produto.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Sku implements Serializable {

    private static final long serialVersionUID = 1L;

    // ent, eu fiz uma imagem dentro de sku também, fiz ela ser local do front, como funciona, se sku tiver uma imagem, sobrescreve a do produto na hora de dar display.
    // ex. se o sku for a camiseta so que de outra cor, muda a cor da imagem, sei lá, posso tirar, só achei útil.
    private static final String CHAVE_IMAGEM = "imagem";

    private Long id;
    private Integer estoque;
    private BigDecimal preco;
    private Map<String, String> especificacoes = new LinkedHashMap<>();
    private Integer pesoGramas;
    private String codigoUniversal;
    private Integer alturaCm;
    private Integer larguraCm;
    private Integer comprimentoCm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Map<String, String> getEspecificacoes() {
        return especificacoes;
    }

    public void setEspecificacoes(Map<String, String> especificacoes) {
        this.especificacoes = especificacoes == null ? new LinkedHashMap<>() : especificacoes;
    }

    public Integer getPesoGramas() {
        return pesoGramas;
    }

    public void setPesoGramas(Integer pesoGramas) {
        this.pesoGramas = pesoGramas;
    }

    public String getCodigoUniversal() {
        return codigoUniversal;
    }

    public void setCodigoUniversal(String codigoUniversal) {
        this.codigoUniversal = codigoUniversal;
    }

    public Integer getAlturaCm() {
        return alturaCm;
    }

    public void setAlturaCm(Integer alturaCm) {
        this.alturaCm = alturaCm;
    }

    public Integer getLarguraCm() {
        return larguraCm;
    }

    public void setLarguraCm(Integer larguraCm) {
        this.larguraCm = larguraCm;
    }

    public Integer getComprimentoCm() {
        return comprimentoCm;
    }

    public void setComprimentoCm(Integer comprimentoCm) {
        this.comprimentoCm = comprimentoCm;
    }

    // Especificações por escrito também.
    @JsonIgnore
    public String getEspecificacoesTexto() {
        StringBuilder texto = new StringBuilder();
        for (Map.Entry<String, String> item : especificacoes.entrySet()) {
            if (texto.length() > 0) {
                texto.append("; ");
            }
            texto.append(item.getKey()).append('=').append(item.getValue());
        }
        return texto.toString();
    }

    @JsonIgnore
    public void setEspecificacoesTexto(String texto) {
        Map<String, String> mapa = new LinkedHashMap<>();
        if (texto != null) {
            for (String par : texto.split(";")) {
                int separador = par.indexOf('=');
                if (separador > 0) {
                    String chave = par.substring(0, separador).trim();
                    String valor = par.substring(separador + 1).trim();
                    if (!chave.isEmpty()) {
                        mapa.put(chave, valor);
                    }
                }
            }
        }
        this.especificacoes = mapa;
    }

    // controle se a imagem realmente é um link.
    @JsonIgnore
    public String getImagemUrl() {
        for (Map.Entry<String, String> item : especificacoes.entrySet()) {
            if (CHAVE_IMAGEM.equalsIgnoreCase(item.getKey().trim()) && item.getValue() != null) {
                String url = item.getValue().trim();
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    return url;
                }
            }
        }
        return null;
    }

    // mostrar as especificações que queremos que sejam visíveis. Não queremos mandar o url da imagem, por exemplo.
    @JsonIgnore
    public String getEspecificacoesVisiveis() {
        StringBuilder texto = new StringBuilder();
        for (Map.Entry<String, String> item : especificacoes.entrySet()) {
            if (CHAVE_IMAGEM.equalsIgnoreCase(item.getKey().trim())) {
                continue;
            }
            if (texto.length() > 0) {
                texto.append("; ");
            }
            texto.append(item.getKey()).append('=').append(item.getValue());
        }
        return texto.toString();
    }

    // um rotulozinho com as coisas.
    @JsonIgnore
    public String getRotulo() {
        String visiveis = getEspecificacoesVisiveis();
        String descricao = visiveis.isEmpty() ? String.valueOf(codigoUniversal) : visiveis;
        return estoque == null ? descricao : descricao + " (estoque: " + estoque + ")";
    }
}
