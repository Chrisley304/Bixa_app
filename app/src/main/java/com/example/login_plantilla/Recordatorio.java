package com.example.login_plantilla;


import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;

import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Recordatorio extends AppCompatActivity {

        Calendar actual = Calendar.getInstance();
        Calendar calendario = Calendar.getInstance();

        String evento;
        private int hora,minutos,dia,mes,anio;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            //setContentView(R.layout.bixa_main);

            anio = actual.get(Calendar.YEAR);
            mes = actual.get(Calendar.MONTH);
            dia = actual.get(Calendar.DAY_OF_MONTH);

            //Valores predefinidos
            int m = 1 ,d = 3, y = 2021;
            calendario.set(Calendar.DAY_OF_MONTH, d);
            calendario.set(Calendar.MONTH, m);
            calendario.set(Calendar.YEAR, y);

            SimpleDateFormat format = new SimpleDateFormat( "dd/MM/yyyy");
            String strDate = format.format(calendario.getTime());
            System.out.println("strDate: "+strDate);

            hora = actual.get(Calendar.HOUR_OF_DAY);
            minutos = actual.get(Calendar.MINUTE);

            //valores predefinidos
            int h = 8 ,min = 27;
            calendario.set(Calendar.HOUR_OF_DAY,h);
            calendario.set(Calendar.MINUTE,min);

            String strHour = String.format("%02d:%02d",h,min);
            System.out.println("strHour: "+ strHour);

            String tag = generateKey();
            Long AlertTime = calendario.getTimeInMillis() - System.currentTimeMillis();
            int random = (int)(Math.random()*50+1);

            Data data = guardarData("Recordatorio con Bixa","Tienes un recordatorio para hoy", random);
            WorkManagerNotificaciones.GuardaN(AlertTime,data,tag);
            Toast.makeText(Recordatorio.this, "Recordatorio establecido", Toast.LENGTH_SHORT).show();

            finish();
        }

        private String generateKey(){
            return UUID.randomUUID().toString();
        }
        private Data guardarData(String titulo, String detalle, int id_noti){
            return new Data.Builder()
                    .putString("titulo", titulo)
                    .putString("detalle", detalle)
                    .putInt("id_noti", id_noti).build();
        }

}


