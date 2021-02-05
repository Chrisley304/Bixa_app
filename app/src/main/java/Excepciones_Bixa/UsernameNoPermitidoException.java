package Excepciones_Bixa;

/**
 * Excepcion creada para cunado el usuario intente crear un perfil con permisos de administrador
 */
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
