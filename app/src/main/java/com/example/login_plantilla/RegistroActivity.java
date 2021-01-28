package com.example.login_plantilla;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import Excepciones_Bixa.CamposIncompletosException;
import Excepciones_Bixa.ContraseniaIncorrectaException;
import Excepciones_Bixa.ContraseniaInseguraException;
import Excepciones_Bixa.UsuarioYaExistenteException;
import Usuarios.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistroActivity extends AppCompatActivity {

    private EditText Edtx_nombres;
    private EditText Edtx_apellidos;
    private EditText Edtx_usuario;
    private EditText Edtx_contra1;
    private EditText Edtx_contra2;
    private RadioButton boton_Masc;
    private RadioButton boton_Fem;
    private Button boton_Registrarse;
    ImageButton FotoPerfil_boton;
    CircleImageView FotoPerfilvista;
    Uri ruta_imagenperfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Se inicializan los atributos de las variables de registro
        Edtx_nombres = findViewById(R.id.entrada_Nombres_registro);
        Edtx_apellidos = findViewById(R.id.entrada_apellidos_registro);
        Edtx_usuario = findViewById(R.id.entrada_usuario_registro);
        Edtx_contra1 = findViewById(R.id.entrada_txf_pass_1);
        Edtx_contra2 = findViewById(R.id.entrada_txf_pass_2);
        boton_Masc = findViewById(R.id.boton_masc_reg);
        boton_Fem = findViewById(R.id.boton_fem_reg);
        boton_Registrarse = findViewById(R.id.boton_registrarse);
        FotoPerfilvista = findViewById(R.id.FotoPerfilVista);

        // Evento para selecionar la foto de perfil
        FotoPerfilvista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });

        boton_Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombres = Edtx_nombres.getText().toString();
                String apellidos = Edtx_apellidos.getText().toString();
                String user = Edtx_usuario.getText().toString();
                String contra = Edtx_contra1.getText().toString();
                String contra2 = Edtx_contra2.getText().toString();
                char sexo = boton_Masc.isChecked()? 'h' : boton_Fem.isChecked()? 'm': '-';

                try {
                    Registrar(user,contra,contra2,nombres,apellidos,sexo);
                } catch (ContraseniaInseguraException e) {
                    Edtx_contra1.setError("La contraseña debe ser de al menos 8 caracteres");
                } catch (UsuarioYaExistenteException e) {
                    Edtx_usuario.setError("El nombre de usuario ya esta registrado en el sistema");
                } catch (ContraseniaIncorrectaException e) {
                    Edtx_contra2.setError("Las contraseñas no coinciden");
                }catch (CamposIncompletosException e){
                    Toast.makeText(RegistroActivity.this,"Por favor, ingresa los datos en todas casillas",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Registrar(String username, String contrasenia,String contra2,  String nombre, String apellido, char genero) throws UsuarioYaExistenteException, ContraseniaInseguraException, ContraseniaIncorrectaException, CamposIncompletosException {

        if (genero == '-'){
            throw new CamposIncompletosException();
        }

        if (nombre.length() <3){
            Edtx_nombres.setError("El nombre debe tener al menos 3 letras");
            throw new CamposIncompletosException();
        }
        if (apellido.length() <3){
            Edtx_apellidos.setError("El apellido debe tener al menos 3 letras");
            throw new CamposIncompletosException();
        }
        if (username.length() <4){
            Edtx_usuario.setError("Debe ser al menos de 4 caracteres");
            throw new CamposIncompletosException();
        }
        // Se verifica que el nombre de usuario no se encuentre registrado actualmente
        if(!BienvenidaActivity.UsuariosRegistrados.containsKey(username)){
            // Si la contraseña es menor a 8 caracteres
            if (contrasenia.length() < 8){
                throw new ContraseniaInseguraException();
            }else{
                // Se verifica que la contraseña escrita en ambas casillas sea la misma
                if (!contrasenia.equals(contra2)){
                    throw new ContraseniaIncorrectaException();
                }else{
                    // si todos los datos son correctos se crea el perfil del usuario y añade a la hash Map
                    BienvenidaActivity.UsuariosRegistrados.put(username,new Usuario(username, contrasenia, nombre, apellido, genero));

                    // Si el usuario ingreso una imagen de perfil
                    if(ruta_imagenperfil != null){
                        BienvenidaActivity.UsuariosRegistrados.get(username).setRuta_fotoperfil(ruta_imagenperfil);
                    }

                    // Se actualiza el archivo de 'base de datos' para que al salir de la app quede registrado el user
                    try {
                        ObjectOutputStream archivo = new ObjectOutputStream( openFileOutput("BasedeUsuarios.bixa", Activity.MODE_PRIVATE));
                        // Añade una HashMap con un usuario admin por defecto
                        archivo.writeObject(BienvenidaActivity.UsuariosRegistrados);
                        archivo.close();
                    } catch (IOException e) {
                        Toast.makeText(this,"ERROR: No se logro actualizar la base de datos",Toast.LENGTH_LONG).show();
                    }
                    // Te envia a la pantalla del asistente
                    Intent intent  = new Intent(RegistroActivity.this, BixaMain.class);
                    intent.putExtra("Usuario",username);
                    startActivity(intent);
                    finish();
                }
            }
        }else{
            throw new UsuarioYaExistenteException();
        }
    }

    // Metodo para utilizar imagenes de el telefono del usuario en la app
    public void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicacion"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(resultCode == RESULT_OK){
            ruta_imagenperfil = data.getData();
            FotoPerfilvista.setImageURI(ruta_imagenperfil);
        }
    }

}