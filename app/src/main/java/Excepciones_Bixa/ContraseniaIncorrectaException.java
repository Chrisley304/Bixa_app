package Excepciones_Bixa;

public class ContraseniaIncorrectaException extends Exception {
    public ContraseniaIncorrectaException() {
        super();
    }

    public ContraseniaIncorrectaException(String message) {
        super(message);
    }
}
