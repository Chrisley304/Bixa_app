package com.example.login_plantilla;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Locale;
import Mensajes.ListaMensajes;
import Mensajes.Mensaje;
import Usuarios.Bixa;
import Usuarios.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class BixaMain extends AppCompatActivity {
    // Variable final requerida para el Speach to text
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    DrawerLayout dwly;

    private RecyclerView mMessageRecycler;
    private ListaMensajes mMessageAdapter;
    private ImageButton Boton_Enviar;
    EditText Texto_porEnviar;
    Usuario user;
    String username;
    ArrayList<Mensaje> messageList = new ArrayList<>();
    FloatingActionButton BotonHablar;
    private TextToSpeech VozBixa;
    CircleImageView BotonFotoperfil,fotopfDrawer;
    Bixa bixa = new Bixa();
    TextView nombreNavbar;
    TextView usernameNavbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bixa_mensajes);
        Boton_Enviar = findViewById(R.id.BotonEnviarMens);
        Texto_porEnviar = findViewById(R.id.Edittxt_mensaje);
        BotonHablar = findViewById(R.id.BotonSpeachtt);
        dwly = findViewById(R.id.DrawerLayout);
        nombreNavbar = findViewById(R.id.Nombre_navbar);
        usernameNavbar = findViewById(R.id.Username_navbar);
        BotonFotoperfil = findViewById(R.id.BotonNavDrawer);
        fotopfDrawer = findViewById(R.id.FotoPerfilDrawer);


        username = getIntent().getStringExtra("Usuario");
        user = BienvenidaActivity.UsuariosRegistrados.get(username);
        String nombreCompleto = user.getNombre() + " " + user.getApellido();
        nombreNavbar.setText(nombreCompleto);
        usernameNavbar.setText(username);

        // Si el usuario agrego foto de perfil, se colocara
        if (user.getRuta_fotoperfil() != null){
            BotonFotoperfil.setImageURI(Uri.fromFile(user.getRuta_fotoperfil()));
            fotopfDrawer.setImageURI(Uri.fromFile(user.getRuta_fotoperfil()));
        }


        // Inicializacion Objeto para el Text to Speach (Voz del Asistente)
        VozBixa = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
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

        BotonFotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickMenu(v);
            }
        });

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
                    Toast.makeText(BixaMain.this,"Por favor ingresa texto",Toast.LENGTH_SHORT).show();
                }
                // 'Vacia' el Edit Text al enviar un mensaje
                Texto_porEnviar.setText("");
            }
        });

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        mMessageRecycler.setHasFixedSize(true );
        LinearLayoutManager linL = new LinearLayoutManager(this);
//        linL.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(linL);
    }

    private void MandarPeticion(String petUsuario, Usuario Perfil_usuario){
        EnviarMensaje(petUsuario,Perfil_usuario);
        petUsuario = petUsuario.toLowerCase();
        String respuesta_bixa = bixa.Responder(petUsuario,Perfil_usuario,getApplicationContext());
        EnviarMensaje(respuesta_bixa,bixa);
        VozBixa.speak(respuesta_bixa,TextToSpeech.QUEUE_FLUSH,null);
    }

    private void EnviarMensaje(String mensaje, Usuario emisor){
        messageList.add(new Mensaje(mensaje,emisor,System.currentTimeMillis()));
        mMessageAdapter = new ListaMensajes(this, messageList);
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
                MandarPeticion(salida,user);
            }
        }
    }

    public void ClickMenu(View view){
        openDrawer(dwly);
    }

    private void openDrawer(DrawerLayout drawerLayout) {
        // Se abre el menu drawer
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickFotoPerfil(View view){
        closeDrawer(dwly);
    }

    public void closeDrawer(DrawerLayout drawerLayout){
        // Se cierra el menu
        // Pero primero se verifica que este abierto en primer lugar
        if(dwly.isDrawerOpen(GravityCompat.START)){
            // Si esta abierto, se procede a cerrar
            dwly.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickBixa(View view){
        recreate();
    }

    public void ClickEditPerfil(View view){
        // Se redirige a la actividad de Editat Perfil
        Toast.makeText(this,"EN CONSTRUCCION",Toast.LENGTH_SHORT).show();
    }
    public void ClickCerrarSesion(View view){
        // Se redirige a la actividad de Editat Perfil
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar Sesion");
        builder.setMessage("¿Estas seguro que quieres cerrar sesión?, Esto te devolvera a la pantalla de inicio");
        // Boton para cerrar sesion
        builder.setPositiveButton("Cerrar Sesión", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(dwly);
    }
}