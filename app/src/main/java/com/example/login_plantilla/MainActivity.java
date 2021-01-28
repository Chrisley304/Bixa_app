package com.example.login_plantilla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Para que la Splash Screen se vea a Pantalla completa
        setContentView(R.layout.activity_splash_screen);

        // Animaciones de la Splash
        Animation animacion1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);

        ImageView textv = findViewById(R.id.Textsplash);
        ImageView logo = findViewById(R.id.logoBixa_Splash);

        // Los elementos hacen las animaciones
        logo.setAnimation(animacion1);
        textv.setAnimation(animacion2);

        // Cambio de Actividad (Cambia a la pantalla de Login)  <-
        // Se realiza con la funcion Handler y Mediante la interfaz Runnable para hacerlo mediante
        // un Hilo que provoca que la app se cargue en "paralelo" o en el fondo, mientras se muestra
        // la SplashScreen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,BienvenidaActivity.class);
                startActivity(intent);
                // Se coloca finish para terminar el hilo de esta actividad y asi no se pueda "regresar" a la splash screen
                finish();
            }
        },2000);
    }
}