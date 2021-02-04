package com.example.login_plantilla;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.apache.commons.io.FileUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import Excepciones_Bixa.CamposIncompletosException;
import Excepciones_Bixa.ContraseniaIncorrectaException;
import Excepciones_Bixa.ContraseniaInseguraException;
import Excepciones_Bixa.UsuarioYaExistenteException;
import Usuarios.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistroActivity extends AppCompatActivity {

    private TextInputLayout Edtx_nombres;
    private TextInputLayout Edtx_apellidos;
    private TextInputLayout Edtx_usuario;
    private TextInputLayout Edtx_contra1;
    private TextInputLayout Edtx_contra2;
    private RadioButton boton_Masc;
    private RadioButton boton_Fem;
    private Button boton_Registrarse;
    ImageButton FotoPerfil_boton;
    CircleImageView FotoPerfilvista;
    Uri uri_imagenperfil;

    private static final int CODIGO_PERMISO = 101;

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
                // Se "pide permiso" al usuario para utilizar imagenes de su galeria (Por cuestiones de privacidad de android)
                if(ContextCompat.checkSelfPermission(RegistroActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    // Si no se tiene el permiso del usuario
                    ActivityCompat.requestPermissions(RegistroActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODIGO_PERMISO);
                }
                else{
                    // Si se tiene el permiso se procede a cargar imagenes
                    cargarImagen();
                }
            }
        });

        boton_Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombres = Edtx_nombres.getEditText().getText().toString();
                String apellidos = Edtx_apellidos.getEditText().getText().toString();
                String user = Edtx_usuario.getEditText().getText().toString();
                String contra = Edtx_contra1.getEditText().getText().toString();
                String contra2 = Edtx_contra2.getEditText().getText().toString();
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

        // Validacion botones de seleccion de genero
        if (genero == '-'){
            boton_Fem.setError("Selecciona una de las opciones");
            throw new CamposIncompletosException();
        }else{
            boton_Fem.setError(null);
        }

        // Validacion de entrada de nombres
        if (nombre.length() <3){
            // Vuelve a la casilla con error
            Edtx_nombres.setError("El nombre debe tener al menos 3 letras");
            throw new CamposIncompletosException();
        }else{
            // Si no hay errores, coloca a la casilla en su estado normal (Por si antes tenia un error)
            Edtx_nombres.setError(null);
        }

        // Validacion de entrada de apellidos
        if (apellido.length() <3){
            Edtx_apellidos.setError("El apellido debe tener al menos 3 letras");
            throw new CamposIncompletosException();
        }else{
            // Si no hay errores, coloca a la casilla en su estado normal (Por si antes tenia un error)
            Edtx_apellidos.setError(null);
        }

        // Validacion entrada nombre de usuario
        if (username.length() <4){
            Edtx_usuario.setError("Debe ser al menos de 4 caracteres");
            throw new CamposIncompletosException();
        }else{
            // Si no hay errores, coloca a la casilla en su estado normal (Por si antes tenia un error)
            Edtx_usuario.setError(null);
        }

        // Se verifica que el nombre de usuario no se encuentre registrado actualmente
        if(!BienvenidaActivity.UsuariosRegistrados.containsKey(username)){
            // Si la contraseña es menor a 8 caracteres
            if (contrasenia.length() < 8){
                throw new ContraseniaInseguraException();
            }else{
                Edtx_contra1.setError(null);
                // Se verifica que la contraseña escrita en ambas casillas sea la misma
                if (!contrasenia.equals(contra2)){
                    throw new ContraseniaIncorrectaException();
                }else{
                    Edtx_contra2.setError(null);
                    // si todos los datos son correctos se crea el perfil del usuario y añade a la hash Map
                    BienvenidaActivity.UsuariosRegistrados.put(username,new Usuario(username, contrasenia, nombre, apellido, genero));
                    // Si el usuario ingreso una imagen de perfil
                    if(uri_imagenperfil != null){
                        String ruta_imagen = getRealPathFromURI(this,uri_imagenperfil);
                        String nombre_img = getFileName(uri_imagenperfil);
                        // guarda la imagen en los archivos internos de la app, donde igual esta guardada la "base de datos"
                        try {
                            insertInPrivateStorage(nombre_img,ruta_imagen);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BienvenidaActivity.UsuariosRegistrados.get(username).setRuta_fotoperfil(nombre_img);
                    }

                    // Se actualiza el archivo de 'base de datos' para que al salir de la app quede registrado el user
                    try {
                        ObjectOutputStream archivo = new ObjectOutputStream( openFileOutput("BasedeUsuarios.bixa", Activity.MODE_PRIVATE));
                        // Añade una HashMap con un usuario admin por defecto
                        archivo.writeObject(BienvenidaActivity.UsuariosRegistrados);
                        archivo.close();
                    } catch (IOException e) {
                        Toast.makeText(RegistroActivity.this,"ERROR: No se logro actualizar la base de datos",Toast.LENGTH_LONG).show();
                    }
                    // Te envia a la pantalla del asistente
                    Intent intent  = new Intent(RegistroActivity.this, BixaMain.class);
                    intent.putExtra("Usuario",username);
                    startActivity(intent);
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
            uri_imagenperfil = data.getData();
            FotoPerfilvista.setImageURI(uri_imagenperfil);
        }
    }

    /* Metodos para guardar imagenes que el usuario ingrese en el backend de la app, autor de estos metodos: haroon47
    * Repositorio de donde se obtuvo: https://github.com/haroon47/ImagesInternalStorage */

    private void insertInPrivateStorage(String name, String path) throws IOException {
        FileOutputStream fos  = openFileOutput(name,MODE_APPEND);

        File file = new File(path);

        byte[] bytes = getBytesFromFile(file);

        fos.write(bytes);
        fos.close();

        Toast.makeText(getApplicationContext(),"File saved in :"+ getFilesDir() + "/"+name,Toast.LENGTH_SHORT).show();


    }

    private byte[] getBytesFromFile(File file) throws IOException {
        byte[] data = FileUtils.readFileToByteArray(file);
        return data;

    }

    private String getFileName(Uri uri)
    {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getRealPathFromURI(Context context, Uri uri)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null,
                null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }

}