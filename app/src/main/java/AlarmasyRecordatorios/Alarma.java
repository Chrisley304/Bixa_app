package AlarmasyRecordatorios;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;

import androidx.appcompat.app.AppCompatActivity;


public class Alarma {

    public static void NuevaAlarma(Context context) {
        // Valores predeterminados
        String Hora = "13";
        String Minutos = "21";
        int hora = Integer.parseInt(Hora);
        int minute = Integer.parseInt(Minutos);
        String bixa_Alarm = "Alarma establecida por BIXA";

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hora);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, bixa_Alarm);

        if (hora <= 23 && minute <= 59) {
            System.out.println("ALARMA" + hora + ":" + minute);
            context.startActivity(intent);
        }
    }

}
