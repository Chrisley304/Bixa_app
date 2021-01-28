package com.example.login_plantilla;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    private EditText usuario_in;
    private EditText password_in;
    private Button boton_login;

    // Hash Map que contendra Usuarios Key -> Username, Value -> password

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
                String usuario = usuario_in.getText().toString();
                String contra = password_in.getText().toString();
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

    private void IniciarSesion(String usuario, String contrasenia) throws UsuarioNoEncontradoException, CamposIncompletosException, ContraseniaIncorrectaException {

        // Se verifica que las variables usuario y contraseña no esten vacias:
        if (usuario.isEmpty()) {
            usuario_in.setError("Introduce el nombre de usuario");
            throw new CamposIncompletosException();
        }
        if (contrasenia.isEmpty()) {
            password_in.setError("Introduce la contraseña");
            throw new CamposIncompletosException();
        }else {
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