package Excepciones_Bixa;

/**
 * Excepcion creada para cuando haya datos sin llenar en un formulario
 */
public class CamposIncompletosException extends IllegalArgumentException {
    public CamposIncompletosException() {
        super();
    }

    public CamposIncompletosException(String s) {
        super(s);
    }
}
