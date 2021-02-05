package AlarmasyRecordatorios;


import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.UUID;
/**
 * Esta clase contiene el algoritmo para que Bixa pueda crear un recordatorio (En forma de notificacion)
 */
public class Recordatorios {

    /**
     * Esta funcion contiene parte del algoritmo para establecer el recordatorio
     * @param context Contiene el contexto de la actividad, la cual se requiere para utilizar metodos de esta
     * @param datos Es un arrayList el cual contiene los datos que el usuario le dio a bixa para que cree su recordatorio
     * @return Regresa una cadena con el mensaje de Bixa, indicandp si el proceso fue correcto o no
     */
    static public String EstablecerRecor(Context context, String[] datos ) {
        Calendar actual = Calendar.getInstance();
        Calendar calendario = Calendar.getInstance();
        int anio = actual.get(Calendar.YEAR);
        int mes = actual.get(Calendar.MONTH);

        int dia, hora, minutos;
        String recordatorio;

        // Validacion de datos
        try{
            // Se obtiene el dia
            if(datos[0].equals("mañana")){
                dia = actual.get(Calendar.DAY_OF_MONTH) + 1 ;
                if (dia > 28){
                    if (mes != 1){
                        dia = dia > 30? 1: dia;
                        if (dia == 1){
                            if(mes != 11){
                                mes++;
                            }else{
                                mes = 0;
                                anio++;
                            }
                        }
                    }
                    // caso febrero
                    else {
                        dia = 1;
                        mes++;
                    }
                }

            }else {
                if (datos[0].equals("hoy")){
                    dia = actual.get(Calendar.DAY_OF_MONTH);
                }else{
                    dia = Integer.parseInt(datos[0]);
                }
            }

            // Se obtiene la hora, separando el 2do dato del arreglo
            StringTokenizer stk = new StringTokenizer(datos[1],":");
            if (stk.countTokens() != 2){
                throw new NumberFormatException();
            }

            hora = Integer.parseInt(stk.nextToken());
            // Se obtienen los minutos
            minutos = Integer.parseInt(stk.nextToken());
            // Se obtiene el recordatorio
            recordatorio = datos[2];

        }catch (NumberFormatException | NullPointerException e){
            return "Lo siento, no pude crear tu recordatorio.\nRecuerda que para que pueda crear un recordatorio por ti, debes ingresarlo de la siguiente forma:\n" +
                    "[dia] [hora:min] [Evento a recordar]";
        }

        //Se define cuando el usuario va a requerir la notificacion
        calendario.set(Calendar.DAY_OF_MONTH, dia);
        calendario.set(Calendar.MONTH, mes);
        calendario.set(Calendar.YEAR, anio );

        //Se define la hora en la que sonara el recordatorio

        calendario.set(Calendar.HOUR, hora);
        calendario.set(Calendar.MINUTE, minutos);

        String tag = generateKey();
        // Obtiene en cunato tiempo se mostrara el recordatorio (Notificacion)
        // Restando [Tiempo elegido por el usuario] - [Tiempo actual]
        Long AlertTime = calendario.getTimeInMillis() - System.currentTimeMillis();
        int random = (int) (Math.random() * 50 + 1);

        Data data = guardarData("Recordatorio con Bixa", recordatorio , random);
        WorkManagerNotificaciones.GuardaN(AlertTime, data, tag);
        SimpleDateFormat format = new SimpleDateFormat( "dd/MM/yyyy");
        String strDate = format.format(calendario.getTime());
        String strHour = String.format("%02d:%02d",hora,minutos);

        return "Te recordare " + recordatorio + " el " + strDate +  " a las " + strHour;
    }

    /**
     * Genera la llave requerida para la notificacion
     * @return regresa la llave en forma de String
     */
    private static String generateKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * Metodo en el cual se crea la notificacion
     * @param titulo Contiene el titulo de la notificacion
     * @param detalle Contiene lo que el usuario solicito que se le recordara
     * @param id_noti Numero random
     * @return Regresa la notificacion en un objeto Data
     */
    private static Data guardarData(String titulo, String detalle, int id_noti) {
        return new Data.Builder()
                .putString("titulo", titulo)
                .putString("detalle", detalle)
                .putInt("id_noti", id_noti).build();
    }

}


