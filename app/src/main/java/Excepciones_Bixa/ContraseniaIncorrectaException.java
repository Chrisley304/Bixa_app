package Excepciones_Bixa;

/**
 * Excepcion creada para cuando el usuario ingresa una contraseña diferente a otra registrada
 */
public class ContraseniaIncorrectaException extends Exception {
    public ContraseniaIncorrectaException() {
        super();
    }

    public ContraseniaIncorrectaException(String message) {
        super(message);
    }
}
