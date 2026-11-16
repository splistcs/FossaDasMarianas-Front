package fdmfront.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


// configurações basicasdo front.
public final class Configuracao {

    private static final String ARQUIVO_PROPRIEDADES = "/fdmfront.properties";
    private static final Properties PROPRIEDADES = carregar();

    private Configuracao() {
    }

    public static String obter(String chave, String padrao) {
        String sistema = System.getProperty(chave);
        if (sistema != null && !sistema.isBlank()) {
            return sistema;
        }
        return PROPRIEDADES.getProperty(chave, padrao);
    }

    private static Properties carregar() {
        Properties propriedades = new Properties();
        try (InputStream entrada = Configuracao.class.getResourceAsStream(ARQUIVO_PROPRIEDADES)) {
            if (entrada != null) {
                propriedades.load(entrada);
            }
        } catch (IOException e) {
            // não era pra chegar aqui.
        }
        return propriedades;
    }
}
