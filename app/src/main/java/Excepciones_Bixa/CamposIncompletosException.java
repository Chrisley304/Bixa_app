package Excepciones_Bixa;

public class CamposIncompletosException extends IllegalArgumentException {
    public CamposIncompletosException() {
        super();
    }

    public CamposIncompletosException(String s) {
        super(s);
    }
}
