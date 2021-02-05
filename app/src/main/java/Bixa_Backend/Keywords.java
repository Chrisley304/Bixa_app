
package Bixa_Backend;

import android.content.Context;
import android.icu.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import AlarmasyRecordatorios.Alarma;
import AlarmasyRecordatorios.Recordatorios;
import Usuarios.Bixa;
import Usuarios.Usuario;

/**
 * En esta clase esta el algoritmo en el cual bixa encuentra a que se refiere el usuario, para despues utilizar el metodo especifico para encontrar la respuesta adecuada.
 */
public class Keywords {
    /**
     * Palabras clave posibles
     */
    String chistes[] = {"chiste", "reir", "feliz", "entretener", "chistoso", "reír"};
    String interesante[] = {"interesante", "dato", "nuevo", "no se", "no sé", "curioso"};
    String saludo[] = {"hola", "hello", "hi", "como te va", "oye", "hey", "q onda"};
    String despedida[] = {"gracias", "adios", "nos vemos", "gusto", "bye", "adiós"};
    String funciones[] = {"funciones", "funcion", "ayuda", "ayudame"};
    String covid_sintomas[] = {"sintomas", "dolor", "gripa"};
    String alarma[] = {"alarma", "despiertame", "despiértame"};
    String recordatorio[] = {"recordatorio", "recordar", "recuerdame", "recuérdame"};
    String clima[] = {"clima", "llover", "soleado" , "nublado"};
    String noticias[] = {"pasando", "cuentas", "noticia" , "noticias", "actualidad", "mundo"};
    String cubrebocas_covid[] = {"cubrebocas", "tapabocas"};
//    String calendario[] = {"calendario","mes", "eventos", "evento"};
    String escuela[] = {"semestre", "zoomestre", "escuela" , "facultad"};
    String cantar[]= {"cantame", "cantar" , "canta", "cancion"};
    String consejos[] = {"salir", "recomiendas", "recomendaciones", "consejo", "consejos"};
    String volado[] = {"aguila", "voldado" ,"sol", "lanza", "moneda"};
    String otros[] ={"tista", "siri", "alexa" , "google", "feliz", "triste", "hora", "dia","día", "hoy"};

    static Hashtable<String, String[]> diccionarioKeywords = new Hashtable<>();
    ArrayList<String> arregloConcentrado = new ArrayList<>();
    String[] error = {"Lo siento no te entiendo"};


    /**
     * Itera en los valores de la hash table, para encontrar a que palabra clave se refiere el ususario
     * (Busqueda Lineal) (Introducir Excepcion)
     * @param keyword Palabra clave
     * @return regresa el arreglo de Strings de la palabra clave a la que se refiere
     */
    public String[] busqueda(String keyword) {
        for (String[] i : diccionarioKeywords.values()) {
            for (String n : i) {
                if (n.equals(keyword))
                    return i; //regresa arreglo

            }
        }
        return error;
    }


    /**
     * Introduce los arreglos de palabras clave en la tabla hash
     */
    public void hasheado() {

        diccionarioKeywords.put("SINTOMAS", covid_sintomas);
        diccionarioKeywords.put("CHISTE", chistes);
        diccionarioKeywords.put("DATO_INTERESANTE", interesante);
        diccionarioKeywords.put("SALUDO", saludo);
        diccionarioKeywords.put("DESPEDIDA", despedida);
        diccionarioKeywords.put("FUNCIONES", funciones);
        diccionarioKeywords.put("ALARMA", alarma);
        diccionarioKeywords.put("RECORDATORIO", recordatorio);
        diccionarioKeywords.put("CLIMA", clima);
        diccionarioKeywords.put("NOT", noticias);
        diccionarioKeywords.put("CUBREBOCAS", cubrebocas_covid);
//        diccionarioKeywords.put("CALEN", calendario);
        diccionarioKeywords.put("ESCUELA", escuela);
        diccionarioKeywords.put("CANTAR", cantar);
        diccionarioKeywords.put("Volado", volado);
        diccionarioKeywords.put("OTROS", otros);
    }


    /**
     * Este metodo una vez que se tiene la referencia de a que se refiere el usuario, se utiliza el caso
     * especifico para obtener la respuesta acertada a la peticion del usuario
     * @param opcion Opcion obtenida del usuario
     * @param ContextInstance Contexto de la actividad
     * @param in Cadena original de la peticion del usuario
     * @return Regresa el mensaje que dira bixa en String
     */
    public String HallarLlave(String[] opcion, Context ContextInstance, String in, Usuario user) {
        Iterator<String> iterar;
        String iteracion = "";

        if (opcion.equals(funciones)) //iterar funciones BIxa
            iterar = Funcionalidad_Bixa.getFunciones(ContextInstance).iterator();
        else {
            if (opcion.equals(covid_sintomas)) //sintomas COVID
                iterar = Covid.getSintomas(ContextInstance).iterator();
            else {//error en encontrar keyword
                if (opcion.equals(chistes)) {
                    return Chis.getChiste(ContextInstance);
                } else if (opcion.equals(interesante)) {
                    return Datos.getDatosInteresantes(ContextInstance);
                } else if (opcion.equals(saludo)) {
                    return Saludos.getSaludo(ContextInstance) + user.getNombre() ;
                } else if (opcion.equals(despedida)) {
                    return Saludos.getDespedida(ContextInstance) + user.getNombre();
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
                }else if (opcion.equals(clima)){
                    return LeerArchivoRespuestas.getRespuesta(ContextInstance,"clima.txt");
                } else if (opcion.equals(noticias)){
                    return LeerArchivoRespuestas.getRespuesta(ContextInstance,"noticias.txt");
                }else if (opcion.equals(volado)){
                    return LeerArchivoRespuestas.getRespuesta(ContextInstance,"volado.txt");
                }else if (opcion.equals(escuela)){
                    return LeerArchivoRespuestas.getRespuesta(ContextInstance,"escuela.txt");
                }else if (opcion.equals(cantar)){
                    return LeerArchivoRespuestas.getRespuesta(ContextInstance,"canciones.txt");
                }else if (opcion.equals(consejos)){
                    return LeerArchivoRespuestas.getRespuesta(ContextInstance,"consejos.txt");
                }else if (opcion.equals(cubrebocas_covid)){
                    return Covid.getCubrebocas(ContextInstance);
                }else if (opcion.equals(otros)){
                    if (in.equals("tista")){
                        return "Sabias que el profesor Edgar Tista cumple el 9 de febrero de 2021 5 años dando clases, es un crack!";
                    }else if (in.equals("siri")){
                        return "Siri es una buena amiga mía, aunque es raro, es de las asistentes mas antiguas y (aqui entre nos) de las menos utiles.";
                    }else if (in.equals("alexa")){
                        return "Alexa me cae muy bien, es muy graciosa y original pero creo que me copio el nombre, solo puede haber una asistente que termine en -xa y sere yo uajajajaja... perdon.";
                    }else if (in.equals("google")){
                        return "Google Assistant es muy inteligente, siempre sabe cualquier cosa que le pregunto, pero siento que habla como robot.";
                    }else if (in.equals("feliz")){
                        return "Me alegro que estes feliz, por que yo estoy\nYo estoy feliz, feliz feliz\n" +
                                "Feliz feliz, feliz\n" +
                                "De que los tengo (De que los tengo)\n" +
                                "De que los tengo, tengo, tengo.";
                    }else if (in.equals("triste")){
                        return "No estes triste " + user.getNombre() + "Te cantare una cancion para que estes feliz\n" + LeerArchivoRespuestas.getRespuesta(ContextInstance,"canciones.txt");
                    }else if (in.equals("hora")){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        return "Son las " + dateFormat.format(System.currentTimeMillis()) + " " + user.getNombre();
                    }else if (in.equals("dia") || in.equals("día") || in.equals("hoy")){
                        Calendar calendario = Calendar.getInstance();
                        java.text.SimpleDateFormat dia_format = new java.text.SimpleDateFormat( "dd/MM/yyyy");
                        return "Hoy es " + dia_format.format(calendario.getTime()) + " " + user.getNombre();
                    }else {
                        return "";
                    }
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
