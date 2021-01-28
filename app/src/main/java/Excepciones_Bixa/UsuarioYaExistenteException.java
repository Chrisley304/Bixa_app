package Excepciones_Bixa;

public class UsuarioYaExistenteException extends Exception{
    public UsuarioYaExistenteException() {
    }

    public UsuarioYaExistenteException(String message) {
        super(message);
    }

    public UsuarioYaExistenteException(String message, Throwable cause) {
        super(message, cause);
    }
}
