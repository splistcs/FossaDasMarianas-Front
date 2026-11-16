package fdmfront.config;

// Espero não chegar aqui, mas preciso pra meios de teste.

public class ApiException extends Exception {

    private static final long serialVersionUID = 1L;

    private final int status;

    public ApiException(int status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public ApiException(int status, String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
