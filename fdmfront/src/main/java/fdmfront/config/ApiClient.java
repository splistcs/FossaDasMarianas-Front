package fdmfront.config;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class ApiClient {

    private static final String CHAVE_URL = "fdm.api.url";
    private static final String URL_PADRAO = "http://localhost:8081/fdm"; // colocando o endereço da api pra ele saber onde buscar.
    private static final Logger LOG = Logger.getLogger(ApiClient.class.getName());
    private static final String JSON = "application/json";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .build();

    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Inject
    private TokenSessao tokenSessao;

    private final String urlBase = Configuracao.obter(CHAVE_URL, URL_PADRAO);


    public <T> T obter(String caminho, TypeReference<T> tipo) throws ApiException {
        HttpResponse<String> resposta = enviar(requisicao(caminho).GET().build()); // manda um get pra api e formata.
        return converter(resposta.body(), tipo);
    }

     public <T> Optional<T> obterOpcional(String caminho, Class<T> tipo) throws ApiException { // um get com tratamento de erro, so pra se vier null responder 404.
        try {
            String corpo = enviar(requisicao(caminho).GET().build()).body();
            if (corpo == null || corpo.isBlank() || "null".equals(corpo.trim())) {
                return Optional.empty();
            }
            return Optional.of(mapper.readValue(corpo, tipo));
        } catch (ApiException e) {
            if (e.getStatus() == 404) {
                return Optional.empty();
            }
            throw e;
        } catch (JsonProcessingException e) {
            throw new ApiException(0, "Resposta inválida da API.", e);
        }
    }

    /** Executa POST e devolve o recurso criado. */
    public <T> T criar(String caminho, Object corpo, Class<T> tipo) throws ApiException {
        HttpRequest requisicao = requisicao(caminho)
                .header("Content-Type", JSON)
                .POST(HttpRequest.BodyPublishers.ofString(serializar(corpo), StandardCharsets.UTF_8))
                .build();
        return converter(enviar(requisicao).body(), tipo);
    }

    /** Executa POST sem corpo e converte a resposta (ex.: aprovar uma solicitação). */
    public <T> T postar(String caminho, Class<T> tipo) throws ApiException {
        HttpRequest requisicao = requisicao(caminho)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        return converter(enviar(requisicao).body(), tipo);
    }

    /** Executa POST sem corpo e ignora a resposta (ex.: recusar uma solicitação). */
    public void postar(String caminho) throws ApiException {
        enviar(requisicao(caminho).POST(HttpRequest.BodyPublishers.noBody()).build());
    }

    /** Executa POST sem corpo e devolve a resposta como texto (ex.: URL de pagamento). */
    public String postarComTexto(String caminho) throws ApiException {
        return enviar(requisicao(caminho).POST(HttpRequest.BodyPublishers.noBody()).build()).body();
    }

    /** Executa PUT sem corpo (parâmetros na URL) e converte a resposta. */
    public <T> T atualizarSemCorpo(String caminho, Class<T> tipo) throws ApiException {
        HttpRequest requisicao = requisicao(caminho)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        return converter(enviar(requisicao).body(), tipo);
    }

    /** Executa PUT e devolve a mensagem de texto da API. */
    public String atualizar(String caminho, Object corpo) throws ApiException {
        HttpRequest requisicao = requisicao(caminho)
                .header("Content-Type", JSON)
                .PUT(HttpRequest.BodyPublishers.ofString(serializar(corpo), StandardCharsets.UTF_8))
                .build();
        return enviar(requisicao).body();
    }

    /** Executa DELETE e devolve a mensagem de texto da API. */
    public String excluir(String caminho) throws ApiException {
        return enviar(requisicao(caminho).DELETE().build()).body();
    }

    private HttpRequest.Builder requisicao(String caminho) {
        HttpRequest.Builder construtor = HttpRequest.newBuilder(URI.create(urlBase + caminho))
                .timeout(TIMEOUT)
                .header("Accept", JSON);
        String token = tokenSessao.getToken();
        if (token != null) {
            construtor.header("Authorization", "Bearer " + token);
        }
        return construtor;
    }

    private HttpResponse<String> enviar(HttpRequest requisicao) throws ApiException {
        try {
            HttpResponse<String> resposta =
                    httpClient.send(requisicao, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (resposta.statusCode() >= 400) {
                LOG.log(Level.WARNING, "{0} {1} -> {2} {3}", new Object[] {
                    requisicao.method(), requisicao.uri(), resposta.statusCode(), resposta.body() });
                throw new ApiException(resposta.statusCode(), mensagemDeErro(resposta));
            }
            return resposta;
        } catch (IOException e) {
            throw new ApiException(0, "Não foi possível conectar à API em " + urlBase + ".", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException(0, "Requisição interrompida.", e);
        }
    }

    /**
     * Extrai uma mensagem legível do erro: usa "detail" (e o mapa "erros") do
     * ProblemDetail da API; para outros formatos, o texto devolvido.
     */
    private String mensagemDeErro(HttpResponse<String> resposta) {
        String corpo = resposta.body();
        if (corpo == null || corpo.isBlank()) {
            return "A API respondeu com o status " + resposta.statusCode() + ".";
        }
        try {
            JsonNode json = mapper.readTree(corpo);
            if (json.hasNonNull("detail")) {
                StringBuilder mensagem = new StringBuilder(json.get("detail").asText());
                JsonNode erros = json.get("erros");
                if (erros != null && erros.isObject()) {
                    for (var erro : erros.properties()) {
                        mensagem.append(' ').append(erro.getKey()).append(": ")
                                .append(erro.getValue().asText()).append(';');
                    }
                }
                return mensagem.toString();
            }
            if (json.hasNonNull("error")) {
                return json.get("error").asText();
            }
        } catch (JsonProcessingException e) {
            // O corpo não é JSON: usa o texto como veio.
        }
        return corpo;
    }

    private String serializar(Object corpo) throws ApiException {
        try {
            return mapper.writeValueAsString(corpo);
        } catch (JsonProcessingException e) {
            throw new ApiException(0, "Não foi possível montar a requisição.", e);
        }
    }

    private <T> T converter(String corpo, TypeReference<T> tipo) throws ApiException {
        try {
            return mapper.readValue(corpo, tipo);
        } catch (JsonProcessingException e) {
            throw new ApiException(0, "Resposta inválida da API.", e);
        }
    }

    private <T> T converter(String corpo, Class<T> tipo) throws ApiException {
        try {
            return mapper.readValue(corpo, tipo);
        } catch (JsonProcessingException e) {
            throw new ApiException(0, "Resposta inválida da API.", e);
        }
    }
}
