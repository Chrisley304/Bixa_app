package Excepciones_Bixa;

/**
 * Excepcion creada para cuando el usuario ingresa un nombre de usuario que no se encuentra en la base de datos
 */
public class UsuarioNoEncontradoException extends Exception{
    public UsuarioNoEncontradoException() {
        super();
    }

    public UsuarioNoEncontradoException(String message) {
        super(message);
    }
}
