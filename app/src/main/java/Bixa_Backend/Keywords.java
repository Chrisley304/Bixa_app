
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
    String chistes[] = {"chiste", "reir", "feliz", "entretener", "chistoso", "reír"};
    String interesante[] = {"interesante", "dato", "nuevo", "no se", "no sé", "aprender"};
    String saludo[] = {"hola", "hello", "hi", "como te va", "oye", "hey", "q onda"};
    String despedida[] = {"gracias", "adios", "nos vemos", "gusto", "bye", "adiós"};
    String funciones[] = {"funciones", "haces", "hacer", "puedes", "poder", "podrias", "funcion", "ayuda", "servir", "sirves", "podrías"};
    String covid[] = {"covid", "pandemia", "cuarentena", "consejo", "recomendacion", "recomendaciones", "recomendación"};
    String alarma[] = {"alarma", "despiertame", "despiértame"};
    String recordatorio[] = {"recordatorio", "recordar", "recuerdame", "recuérdame"};

    static Hashtable<String, String[]> diccionarioKeywords = new Hashtable<>();
    ArrayList<String> arregloConcentrado = new ArrayList<>();
    String[] error = {"Lo siento no te entiendo"};

    public void unirArrays(String[] arr) {
        for (String i : arr)
            arregloConcentrado.add(i);
    }

    // Itera en los valores de la hash table, para encontrar a que palabra clave se refiere el ususario
    // (Busqueda Lineal) (Introducir Excepcion)
    public String[] busqueda(String keyword) {
        for (String[] i : diccionarioKeywords.values()) {
            for (String n : i) {
                if (n.equals(keyword))
                    return i; //regresa arreglo

            }
        }
        return error;
    }


    // Introduce los arreglos de palabras clave en la tabla hash
    public void hasheado() {

        diccionarioKeywords.put("COVID", covid);
        diccionarioKeywords.put("CHISTE", chistes);
        diccionarioKeywords.put("DATO_INTERESANTE", interesante);
        diccionarioKeywords.put("SALUDO", saludo);
        diccionarioKeywords.put("DESPEDIDA", despedida);
        diccionarioKeywords.put("FUNCIONES", funciones);
        diccionarioKeywords.put("ALARMA", alarma);
        diccionarioKeywords.put("RECORDATORIO", recordatorio);
    }


    // 
    public String HallarLlave(String[] opcion, Context ContextInstance, String in) {
        Iterator<String> iterar;
        String iteracion = "";

        if (opcion.equals(funciones)) //iterar funciones BIxa
            iterar = Funcionalidad_Bixa.getFunciones(ContextInstance).iterator();
        else {
            if (opcion.equals(covid)) //sintomas COVID
                iterar = Covid.getSintomas(ContextInstance).iterator();
            else {//error en encontrar keyword
                if (opcion.equals(chistes)) {
                    return Chis.getChiste(ContextInstance);
                } else if (opcion.equals(interesante)) {
                    return Datos.getDatosInteresantes(ContextInstance);
                } else if (opcion.equals(saludo)) {
                    return Saludos.getSaludo(ContextInstance);
                } else if (opcion.equals(despedida)) {
                    return Saludos.getDespedida(ContextInstance);
                } else if (opcion.equals(alarma)) {
                    StringTokenizer stok = new StringTokenizer(Bixa.Peticion, " ");
                    // buscara la hora de la alarma en formato 24h
                    String hora = "";
                    boolean sigue = false;
                    while (stok.hasMoreTokens()) {
                        String token = stok.nextToken();
                        if (sigue) {
                            if (!token.equals("a") && !token.equals("las") && !token.equals("el") && !token.equals("en")) {
                                hora = token;
                                sigue = false;
                            }
                        }
                        sigue = token.equals(in)? true : sigue;
                    }
                    return Alarma.NuevaAlarma(ContextInstance, hora);

                } else if (opcion.equals(recordatorio)) {
                    StringTokenizer stok = new StringTokenizer(Bixa.Peticion, " ");
                    // Arreglo de cadenas donde se alamcenara la peticion de la siguiente forma:
                    // [Cuando (día)] , [hora:minutos], [evento]
                    String[] datos = new String[3];
                    datos[2] = "";
                    int cont = 0;
                    while (stok.hasMoreTokens()) {
                        String token = stok.nextToken();
                        if (cont > 0) {
                            if (!token.equals("a") && !token.equals("las") && !token.equals("el") && !token.equals("que") && !token.equals("en")) {
                                if (cont < 3) {
                                    datos[cont - 1] = token;
                                    cont++;
                                } else {
                                    datos[2] += " " + token;
                                }
                            }
                        }
                        cont = token.equals(in) ? 1 : cont;
                    }
                    return Recordatorios.EstablecerRecor(ContextInstance, datos);
                } else {
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
