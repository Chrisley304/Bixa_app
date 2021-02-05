package AlarmasyRecordatorios;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;

import androidx.appcompat.app.AppCompatActivity;

import java.util.StringTokenizer;

/**
 * Esta clase contiene el algoritmo para que Bixa pueda crear una alarma
 */
public class Alarma {

    /**
     * Esta clase estatica contiene el codigo necesario para implementar una alarma
     * y al ser statica, se puede utilizar sin necesidad de instanciar la clase
     * @param context Contiene el contexto de la actividad, la cual se requiere para utilizar metodos de esta
     * @param hora_junta Contiene la hora a la que el usuario le solicito a bixa crear la alarma
     * @return Regresa el mensaje que dira Bixa indicando si el proceso fue correcto o ocurrio algun error
     */
    public static String NuevaAlarma(Context context, String hora_junta) {

        int hora;
        int minutos;
        // Obtiene las horas y los minutos de la solucitud
        try{
            StringTokenizer stk = new StringTokenizer(hora_junta,":");
            if (stk.countTokens() != 2){
                throw new NumberFormatException();
            }

            hora = Integer.parseInt(stk.nextToken());
            // Se obtienen los minutos
            minutos = Integer.parseInt(stk.nextToken());
        }catch (NumberFormatException e){
            return "Lo siento, no pude crear la alarma.\nRecuerda que para que pueda crear una alarma por ti, debes ingresarlo de la siguiente forma:\n" +
                    "[hora:min] (24 h)";
        }


        String bixa_Alarm = "Alarma establecida por BIXA";

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hora);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minutos);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, bixa_Alarm);

        boolean exito = false;
        if (hora <= 23 && minutos <= 59) {
            context.startActivity(intent);
            exito = true;
        }

        String strHour = String.format("%02d:%02d",hora,minutos);

        if (exito){
            // Es en la mañana
            if (hora<11){
                return "OK, te despertare a las " + strHour ;

            }
            return "He establecido la alarma a las " + strHour ;
        }else{
            return "Ha ocurrido un error al crear la alarma, recuerda que tienes que ingrsar la hora en formato de 24 horas\n" +
                    "Ej. 15:30";
        }

    }

}
