
package Bixa_Backend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.io.*;
import java.util.Map;

public class Keywords {
    // Palabras clave posibles
    String chistes[] = {"chiste","reir","feliz","entretener","chistoso"};
    String interesante[] = {"interesante","dato","nuevo","no se","no se","aprender"};
    String saludo[] = {"hola","hello","hi","como te va","oye","hey","q onda"};
    String despedida[] = {"gracias","adios","nos vemos","gusto","bye"};
    String funciones[] = {"funciones","haces","hacer","puedes","poder","podrias","funcion","ayuda","servir","sirves"};
    String covid [] = {"covid","pandemia","cuarentena","consejo", "recomendacion","recomendaciones"};

    static Hashtable <String, String[]> diccionarioKeywords = new Hashtable<>();   
    ArrayList<String> arregloConcentrado = new ArrayList<>();
    String []error = {"Lo siento no te entiendo"};

    public void unirArrays(String[] arr){
        for(String i : arr)
            arregloConcentrado.add(i);     
    }
    
    // Itera en los valores de la hash table, para encontrar a que palabra clave se refiere el ususario
    // (Busqueda Lineal) (Introducir Excepcion)
    public String[] busqueda(String keyword){
        for(String[] i: diccionarioKeywords.values() ){
            for(String n: i){
                if(n.equals(keyword))
                    return i; //regresa arreglo
                
            }
        }
        return error;
    }
    

    // Introduce los arreglos de palabras clave en la tabla hash
    public void hasheado(){
         
        diccionarioKeywords.put("COVID",covid);
        diccionarioKeywords.put("CHISTE",chistes);
        diccionarioKeywords.put("DATO_INTERESANTE",interesante);
        diccionarioKeywords.put("SALUDO",saludo);
        diccionarioKeywords.put("DESPEDIDA",despedida);
        diccionarioKeywords.put("FUNCIONES",funciones);
    }


    // 
    public String HallarLlave(String[] opcion){
        Iterator<String> iterar = null;
        String iteracion = "";

        if(opcion.equals(funciones)) //iterar funciones BIxa
            iterar = Funcionalidad_Bixa.getFunciones().iterator();
        else{
            if(opcion.equals(covid)) //sintomas COVID
                iterar = Covid.getSintomas().iterator();
            else{//error en encontrar keyword
                if(opcion.equals(chistes)){
                    return Chis.getChiste();
                }
                    
                else if(opcion.equals(interesante)){
                    return Datos.getDatosInteresantes();
                }
                    
                else if(opcion.equals(saludo)) {
                    return Saludos.getSaludo();
                }
                else if(opcion.equals(despedida)) {
                    return Saludos.getDespedida();
                }
                else {
                    // No se encontro referencia (Bixa no te entendio)
                    return "";
                }        
                }
            }
        
        while (iterar.hasNext()) 
            iteracion += iterar.next() + "\n";

        return iteracion;
    }       
}
