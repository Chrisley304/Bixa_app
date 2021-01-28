package Usuarios;

public class Bixa extends Usuario{

    public Bixa() {
        super("bixa", "", "Bixa", "Assistant", 'm');
    }

    public String Responder(String peticion_usuario,Usuario usuario){
        return "Hola " + usuario.getNombre() ;
        // palabras clave

    }
}
