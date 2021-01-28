package com.example.login_plantilla;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechtoText extends AppCompatActivity {
    // Variables para Speech to Text
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView nEntradaVoz;
    private ImageButton nBotonHablar;

    // Variables para Text to Sppech
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bixa_main);

        nEntradaVoz = findViewById(R.id.texto_entrada);
        nBotonHablar = findViewById(R.id.boton_Hablar);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if ( status == TextToSpeech.SUCCESS){
                    // Seleccion del idioma
                    int idioma = tts.setLanguage(Locale.getDefault());
                }
            }
        });

        // Evento cuando el boton es pulsado
        nBotonHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se obtiene el texto de la voz
                In_EntradaVoz();
                // Una vez con el texto se vuelve voz

            }
        });
    }

    private void In_EntradaVoz() {
        // Itent -> clase de Google con funciones chidas, como reconocimiento de voz
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Se obtiene el lenguaje por defecto con Locale -> Clase para acceder a datos del Cel
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Bienvenido al very early build de Bixa Assistant\nPor favor, pulsa el microfono para hablar");
        try{
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> resultado = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String salida = resultado.get(0);
                nEntradaVoz.setText(salida);
                tts.speak(salida,TextToSpeech.QUEUE_FLUSH,null);
            }
        }
    }
}