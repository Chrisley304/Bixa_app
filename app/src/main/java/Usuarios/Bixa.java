package Usuarios;

import android.content.Context;
import android.widget.Toast;

import com.example.login_plantilla.BixaMain;

import java.util.StringTokenizer;

import Bixa_Backend.Keywords;

/**
 * Esta clase hereda de la clase Usuario, y es la clase del Asistente virtual
 * Del cual ocupa metodos para interactuar con el usuario
 */
public class Bixa extends Usuario{

    public Bixa() {
        super("bixa", "", "Bixa", "Assistant", 'm');
    }

    public static String Peticion;

    /**
     * Este metodo se utiliza para analizar la cadena que ingreso el usuario y a partir de ello responder
     * lo mas adecuado
     * @param peticion_usuario Incluye la peticion que el usuario le envio a bixa
     * @param usuario Contiene el perfil completo del usuario
     * @param ConstextInstance Contiene el contexto de la actividad para utilizar metodos de esta, ya que esta clase
     *                         al estar fuera de la actividad, no puede acceder a estos
     * @return regresa la respuesta en una String
     */
    public String Responder(String peticion_usuario, Usuario usuario, Context ConstextInstance){
        // Se emplea la funcion String Tokenizer para 'separar' la peticion del usuario y buscar la
        // palabra clave requerida
        StringTokenizer stok = new StringTokenizer(peticion_usuario," ");
        boolean clave_hallada = false;
        String clave = "", respuesta = "";
        // Objeto para buscar palabra clave
        Keywords palabra = new Keywords();
        Peticion = peticion_usuario;
        // Se crea un ciclo que recorre la cadena de la peticion del usuario hasta que se
        // acabe esta, o se encuentre la palabra clave de la peticion
        while(stok.hasMoreTokens() && !clave_hallada ){
            clave = stok.nextToken();
            palabra.hasheado();
            respuesta = palabra.HallarLlave(palabra.busqueda(clave), ConstextInstance,clave,usuario);
            clave_hallada = !respuesta.equals("");
        }

        if (respuesta.equals("")){
            respuesta = "Lo siento, no te entendi.Repitelo de nuevo.\nSi no sabes que puedo hacer, di 'funciones' o 'ayuda'";
        }

        return respuesta;

    }
}
