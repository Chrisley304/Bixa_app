package Excepciones_Bixa;

/**
 * Excepcion creada para cuando el usuario ingrese una contraseña menor a 8 caracteres
 */
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
