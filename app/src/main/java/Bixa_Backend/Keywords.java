
package Bixa_Backend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.login_plantilla.Alarma;
import com.example.login_plantilla.Recordatorio;

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
    String alarma[] = {"alarma"};
    String recordatorio[] = {"recordatorio", "recordar"};

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
        diccionarioKeywords.put("ALARMA", alarma);
        diccionarioKeywords.put("RECORDATORIO",recordatorio);
    }


    // 
    public String HallarLlave(String[] opcion, Context ContextInstance){
        Iterator<String> iterar = null;
        String iteracion = "";

        if(opcion.equals(funciones)) //iterar funciones BIxa
            iterar = Funcionalidad_Bixa.getFunciones(ContextInstance).iterator();
        else{
            if(opcion.equals(covid)) //sintomas COVID
                iterar = Covid.getSintomas(ContextInstance).iterator();
            else{//error en encontrar keyword
                if(opcion.equals(chistes)){
                    return Chis.getChiste(ContextInstance);
                }
                    
                else if(opcion.equals(interesante)){
                    return Datos.getDatosInteresantes(ContextInstance);
                }
                    
                else if(opcion.equals(saludo)) {
                    return Saludos.getSaludo(ContextInstance);
                }
                else if(opcion.equals(despedida)) {
                    return Saludos.getDespedida(ContextInstance);
                }
                else if(opcion.equals(alarma)) {
                    Intent next = new Intent(ContextInstance, Alarma.class);
                    ContextInstance.startActivity(next);
                    return "Alarma establecida";
                }
                else if(opcion.equals(recordatorio)){
                    Intent next = new Intent(ContextInstance, Recordatorio.class);
                    ContextInstance.startActivity(next);
                    return "Se añadio un nuevo recordatorio";
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
