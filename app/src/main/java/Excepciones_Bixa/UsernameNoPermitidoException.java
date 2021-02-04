package Excepciones_Bixa;

public class UsernameNoPermitidoException extends UsuarioYaExistenteException {
    public UsernameNoPermitidoException() {
        super();
    }

    public UsernameNoPermitidoException(String message) {
        super(message);
    }

    public UsernameNoPermitidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
