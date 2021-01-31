package com.example.login_plantilla;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Excepciones_Bixa.CamposIncompletosException;
import Excepciones_Bixa.ContraseniaIncorrectaException;
import Excepciones_Bixa.UsuarioNoEncontradoException;

public class LoginBixa extends Fragment {

    private EditText usuario_in;
    private EditText password_in;
    private Button boton_login;

    public LoginBixa() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_bixa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usuario_in = view.findViewById(R.id.entrada_usuario_login);
        password_in = view.findViewById(R.id.entrada_texto_contra_login);
        boton_login = view.findViewById(R.id.boton_login);


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
                    Toast.makeText(getActivity(),"Por favor, ingresa datos en ambas casillas",Toast.LENGTH_SHORT).show();
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
                    Intent intent  = new Intent(getActivity(), MenuDespegable.class);
                    intent.putExtra("Usuario",usuario);
                    startActivity(intent);
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