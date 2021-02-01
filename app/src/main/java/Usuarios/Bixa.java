package Usuarios;

import android.content.Context;
import android.widget.Toast;

import com.example.login_plantilla.BixaMain;

import java.util.StringTokenizer;

import Bixa_Backend.Keywords;

public class Bixa extends Usuario{

    public Bixa() {
        super("bixa", "", "Bixa", "Assistant", 'm');
    }

    public String Responder(String peticion_usuario, Usuario usuario, Context ConstextInstance){
        // Se emplea la funcion String Tokenizer para 'separar' la peticion del usuario y buscar la
        // palabra clave requerida
        StringTokenizer stok = new StringTokenizer(peticion_usuario," ");
        boolean clave_hallada = false;
        String clave = "", respuesta = "";
        // Objeto para buscar palabra clave
        Keywords palabra = new Keywords();

        // Se crea un ciclo que recorre la cadena de la peticion del usuario hasta que se
        // acabe esta, o se encuentre la palabra clave de la peticion
        while(stok.hasMoreTokens() && !clave_hallada ){
            System.out.println("SI ENTRO AL WHILE");
            clave = stok.nextToken();
            palabra.hasheado();
            respuesta = palabra.HallarLlave(palabra.busqueda(clave), ConstextInstance);
            clave_hallada = !respuesta.equals("");
        }

        if (respuesta.equals("")){
            respuesta = "Lo siento, no te entendi.Repitelo de nuevo";
        }

        return respuesta;

    }
}
