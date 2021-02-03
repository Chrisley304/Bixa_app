package com.example.login_plantilla;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;

import androidx.appcompat.app.AppCompatActivity;


public class Alarma extends AppCompatActivity{
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.);

        // Valores predeterminados
        String Hora = "13";
        String Minutos = "21";
        int hora = Integer.parseInt(Hora);
        int minute = Integer.parseInt(Minutos);
        String bixa_Alarm = "Alarma asignada por BIXA";

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hora);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, bixa_Alarm);

        if (hora <= 23 && minute <= 59) {
            System.out.println("ALARMA" + hora + ":" + minute);
            startActivity(intent);
            finish();
        }
    }
}
