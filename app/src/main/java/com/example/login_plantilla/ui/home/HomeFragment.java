package com.example.login_plantilla.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login_plantilla.BienvenidaActivity;
import com.example.login_plantilla.BixaMain;
import com.example.login_plantilla.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

import Mensajes.ListaMensajes;
import Mensajes.Mensaje;
import Usuarios.Bixa;
import Usuarios.Usuario;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    String username;
    private RecyclerView mMessageRecycler;
    private ListaMensajes mMessageAdapter;
    private ImageButton Boton_Enviar;
    EditText Texto_porEnviar;
    Usuario user;
    ArrayList<Mensaje> messageList = new ArrayList<>();
    FloatingActionButton BotonHablar;
    private TextToSpeech VozBixa;
    Bixa bixa = new Bixa();
    HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Se obtiene el nombre de usuario de la actividad MenuDespegable (El nombre es temporal)
        username = "admin";//getArguments().getString("usuario");

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Boton_Enviar = view.findViewById(R.id.BotonEnviarMens);
        Texto_porEnviar = view.findViewById(R.id.Edittxt_mensaje);
        BotonHablar = view.findViewById(R.id.BotonSpeachtt);

        // Inicializacion Objeto para el Text to Speach (Voz del Asistente)
        VozBixa = new TextToSpeech(getContext().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if ( status == TextToSpeech.SUCCESS){
                    // Seleccion del idioma
                    int idioma = VozBixa.setLanguage(Locale.getDefault());
                }
            }
        });

        // Evento para el boton que hara el Speach to Text
        BotonHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se obtiene el texto de la voz
                In_EntradaVoz();
                // Una vez con el texto se vuelve voz
            }
        });

        // Se obtiene el usuario de la actividad pasada (Registro o Log in)
        user = BienvenidaActivity.UsuariosRegistrados.get(username);

        // Muestra el Boton de enviar solo cuando hay texto, para asi mostrar el boton de Speach to Text
        Texto_porEnviar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    Boton_Enviar.setVisibility(View.GONE);
                    BotonHablar.setVisibility(View.VISIBLE);
                }else{
                    Boton_Enviar.setVisibility(View.VISIBLE);
                    BotonHablar.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        Boton_Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = Texto_porEnviar.getText().toString();

                if(!mensaje.isEmpty()){
                    MandarPeticion(mensaje,user);

                }else {
                    Toast.makeText(getActivity(),"Por favor ingresa texto",Toast.LENGTH_SHORT).show();
                }
                // 'Vacia' el Edit Text al enviar un mensaje
                Texto_porEnviar.setText("");
            }
        });

        mMessageRecycler = (RecyclerView) view.findViewById(R.id.recycler_gchat);
        mMessageRecycler.setHasFixedSize(true );
        LinearLayoutManager linL = new LinearLayoutManager(getContext());
//        linL.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(linL);
    }

    private void MandarPeticion(String petUsuario, Usuario Perfil_usuario){
        EnviarMensaje(petUsuario,Perfil_usuario);
        petUsuario = petUsuario.toLowerCase();
        String respuesta_bixa = bixa.Responder(petUsuario,Perfil_usuario);
        EnviarMensaje(respuesta_bixa,bixa);
        VozBixa.speak(respuesta_bixa,TextToSpeech.QUEUE_FLUSH,null);
    }

    private void EnviarMensaje(String mensaje, Usuario emisor){
        messageList.add(new Mensaje(mensaje,emisor,System.currentTimeMillis()));
        mMessageAdapter = new ListaMensajes(getActivity(), messageList);
        mMessageRecycler.setAdapter(mMessageAdapter);
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
            Toast.makeText(getActivity(), "Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> resultado = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String salida = resultado.get(0);
                MandarPeticion(salida,user);
            }
        }
    }
}