package com.example.login_plantilla;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Inicio_Bixa extends Fragment {


    public Inicio_Bixa() {
        // Required empty public constructor
    }

    // Carga la 'pantalla 'inicial'
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio__bixa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button IniciarSes = view.findViewById(R.id.InciarSesion_boton);
        Button Registrarse = view.findViewById(R.id.Registrarse_boton);

        IniciarSes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se navega a la Actividad de Iniciar Sesion
                Navigation.findNavController(v).navigate(R.id.loginBixa);
            }
        });
        Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se navega a la Actividad de Registrarse
                Navigation.findNavController(v).navigate(R.id.registroBixa);
            }
        });

    }

}