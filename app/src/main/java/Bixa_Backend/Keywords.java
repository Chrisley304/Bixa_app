
package Bixa_Backend;

import android.content.Context;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import AlarmasyRecordatorios.Alarma;
import AlarmasyRecordatorios.Recordatorios;
import Usuarios.Bixa;

public class Keywords {
    // Palabras clave posibles
    String chistes[] = {"chiste","reir","feliz","entretener","chistoso"};
    String interesante[] = {"interesante","dato","nuevo","no se","no se","aprender"};
    String saludo[] = {"hola","hello","hi","como te va","oye","hey","q onda"};
    String despedida[] = {"gracias","adios","nos vemos","gusto","bye"};
    String funciones[] = {"funciones","haces","hacer","puedes","poder","podrias","funcion","ayuda","servir","sirves"};
    String covid [] = {"covid","pandemia","cuarentena","consejo", "recomendacion","recomendaciones"};
    String alarma[] = {"alarma","despiertame"};
    String recordatorio[] = {"recordatorio", "recordar","recuerdame"};

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
    public String HallarLlave(String[] opcion, Context ContextInstance,String in){
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
                    Alarma.NuevaAlarma(ContextInstance);
                    return "Alarma establecida";
                }
                else if(opcion.equals(recordatorio)){
                    StringTokenizer stok = new StringTokenizer(Bixa.Peticion," ");
                    // Arreglo de cadenas donde se alamcenara la peticion de la siguiente forma:
                    // [Cuando (día)] , [hora] , [minutos], [evento]
                    String[] datos = new String[4];
                    int cont = 0;
                    while(stok.hasMoreTokens()){
                        String token = stok.nextToken();
                        if (token.equals(in)  || cont > 0 ){
                            if (!token.equals("a") && !token.equals("las") && !token.equals("el") && !token.equals("que") && !token.equals(":") && !token.equals("en")){
                                if (cont < 3){
                                    datos[cont] = token;
                                    cont++;
                                }else {
                                    datos[3] += token;

                                }
                            }
                        }
                    }
                    return Recordatorios.EstablecerRecor(ContextInstance,datos);
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
