package Excepciones_Bixa;

public class ContraseniaInseguraException extends Exception {
    public ContraseniaInseguraException() {
    }

    public ContraseniaInseguraException(String message) {
        super(message);
    }

    public ContraseniaInseguraException(String message, Throwable cause) {
        super(message, cause);
    }
}
