package com.example.login_plantilla;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.ObjectOutputStream;

import Excepciones_Bixa.CamposIncompletosException;
import Excepciones_Bixa.ContraseniaIncorrectaException;
import Excepciones_Bixa.ContraseniaInseguraException;
import Excepciones_Bixa.UsuarioYaExistenteException;
import Usuarios.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class RegistroBixa extends Fragment {

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
    Uri ruta_imagenperfil;

    private static final int CODIGO_PERMISO = 101;


    public RegistroBixa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro_bixa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Se inicializan los atributos de las variables de registro
        Edtx_nombres = view.findViewById(R.id.entrada_Nombres_registro);
        Edtx_apellidos = view.findViewById(R.id.entrada_apellidos_registro);
        Edtx_usuario = view.findViewById(R.id.entrada_usuario_registro);
        Edtx_contra1 = view.findViewById(R.id.entrada_txf_pass_1);
        Edtx_contra2 = view.findViewById(R.id.entrada_txf_pass_2);
        boton_Masc = view.findViewById(R.id.boton_masc_reg);
        boton_Fem = view.findViewById(R.id.boton_fem_reg);
        boton_Registrarse = view.findViewById(R.id.boton_registrarse);
        FotoPerfilvista = view.findViewById(R.id.FotoPerfilVista);

        // Evento para selecionar la foto de perfil
        FotoPerfilvista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se "pide permiso" al usuario para utilizar imagenes de su galeria (Por cuestiones de privacidad de android)
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    // Si no se tiene el permiso del usuario
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODIGO_PERMISO);
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
                    Toast.makeText(getActivity(),"Por favor, ingresa los datos en todas casillas",Toast.LENGTH_SHORT).show();
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
                    if(ruta_imagenperfil != null){
                        BienvenidaActivity.UsuariosRegistrados.get(username).setRuta_fotoperfil(ruta_imagenperfil);
                    }

                    // Se actualiza el archivo de 'base de datos' para que al salir de la app quede registrado el user
                    try {
                        ObjectOutputStream archivo = new ObjectOutputStream( getContext().openFileOutput("BasedeUsuarios.bixa", Activity.MODE_PRIVATE));
                        // Añade una HashMap con un usuario admin por defecto
                        archivo.writeObject(BienvenidaActivity.UsuariosRegistrados);
                        archivo.close();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(),"ERROR: No se logro actualizar la base de datos",Toast.LENGTH_LONG).show();
                    }
                    // Te envia a la pantalla del asistente
                    Intent intent  = new Intent(getActivity(), MenuDespegable.class);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(resultCode == RESULT_OK){
            ruta_imagenperfil = data.getData();
            FotoPerfilvista.setImageURI(ruta_imagenperfil);
        }
    }
}