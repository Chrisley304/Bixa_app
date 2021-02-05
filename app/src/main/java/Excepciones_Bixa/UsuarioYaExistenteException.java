package Excepciones_Bixa;

/**
 * Excepcion creada para cuando se ingrese un usuario para registrarlo, pero este ya se encuentra en la base de datos
 */
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
