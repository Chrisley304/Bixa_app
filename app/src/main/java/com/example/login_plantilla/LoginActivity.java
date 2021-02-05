package com.example.login_plantilla;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import Excepciones_Bixa.CamposIncompletosException;
import Excepciones_Bixa.ContraseniaIncorrectaException;
import Excepciones_Bixa.UsuarioNoEncontradoException;
import Usuarios.Usuario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Esta clase es la encargada de generar la actividad para Iniciar Sesion
 *
 * @author Christian Leyva, Fernanda Aguilar, Berenice Martinez
 */

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usuario_in;
    private TextInputLayout password_in;
    private Button boton_login;

    /**
     * En este metodo se genera la actividad para iniciar sesion, y se definen las variables.
     * @param savedInstanceState Parametro recibido por defecto por las actividades para su creación.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario_in = findViewById(R.id.entrada_usuario_login);
        password_in = findViewById(R.id.entrada_texto_contra_login);
        boton_login = findViewById(R.id.boton_login);


        boton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = usuario_in.getEditText().getText().toString();
                String contra = password_in.getEditText().getText().toString();
                try {
                    IniciarSesion(usuario,contra);
                } catch (UsuarioNoEncontradoException e) {
                    usuario_in.setError("Usuario Incorrecto");
                } catch (ContraseniaIncorrectaException e) {
                    password_in.setError("Contraseña incorrecta");
                } catch (CamposIncompletosException e){
                    Toast.makeText(LoginActivity.this,"Por favor, ingresa datos en ambas casillas",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * En este metodo se lee de las casillas el nombre de usuario y la contraseña del mismo, para si
     * coinciden con la informacion de la 'base de datos' permitirle acceder a la actividad de BixaMain
     * Si no es correcto, se le manda un mensaje de error al usuario en la casiila donde se encuentra el problema
     * @param usuario Nombre de usuario ingresado
     * @param contrasenia Contraseña ingresada
     * @throws UsuarioNoEncontradoException Se lanza esta Excepcion en el caso en que el nombre de usuario que se ingreso no se encuentre en la base de datos
     * @throws CamposIncompletosException Excepcion lanzada en caso de que el usuario no haya llenado todos los campos de el formulario.
     * @throws ContraseniaIncorrectaException Excepcion lanzada en caso de que si se halla encontrado el nombre de usuario, pero la contraseña ingresada sea incorrecta
     */
    private void IniciarSesion(String usuario, String contrasenia) throws UsuarioNoEncontradoException, CamposIncompletosException, ContraseniaIncorrectaException {

        // Se verifica que las variables usuario y contraseña no esten vacias:
        if (usuario.isEmpty()) {
            usuario_in.setError("Introduce el nombre de usuario");
            throw new CamposIncompletosException();
        }else{
            usuario_in.setError(null);
        }

        if (contrasenia.isEmpty()) {
            password_in.setError("Introduce la contraseña");
            throw new CamposIncompletosException();
        }else {
            password_in.setError(null);
            // Si el usuario se encuentra en la base de datos:
            if(BienvenidaActivity.UsuariosRegistrados.containsKey(usuario)){
                // Si la contraseña es correcta:
                if (BienvenidaActivity.UsuariosRegistrados.get(usuario).getContrasenia().equals(contrasenia)){
                    // Se inicia la actividad con el asistente de voz
                    Intent intent  = new Intent(LoginActivity.this, BixaMain.class);
                    intent.putExtra("Usuario",usuario);
                    startActivity(intent);
                    finish();
                }
                // Contraseña incorrecta
                else{
                    throw new ContraseniaIncorrectaException();
                }
            }else {
                 throw new UsuarioNoEncontradoException();
            }
        }
    }
}