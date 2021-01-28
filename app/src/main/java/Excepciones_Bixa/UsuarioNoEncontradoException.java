package Excepciones_Bixa;

public class UsuarioNoEncontradoException extends Exception{
    public UsuarioNoEncontradoException() {
        super();
    }

    public UsuarioNoEncontradoException(String message) {
        super(message);
    }
}
