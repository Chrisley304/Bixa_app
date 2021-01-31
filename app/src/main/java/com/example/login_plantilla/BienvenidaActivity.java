package com.example.login_plantilla;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import Usuarios.Usuario;

public class BienvenidaActivity extends AppCompatActivity {

    public static HashMap<String,Usuario> UsuariosRegistrados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_layout);

        String fileName = "BasedeUsuarios.bixa";
        // Instancia File, que servira para verificar la existencia del archivo
        File base_datos = new File(getFilesDir(), fileName);
        if (!base_datos.exists()) { // Si el archivo de objetos que servira como "base de datos" aun no existe, lo crea
            Toast.makeText(this, "Se va a crear la base de datos", Toast.LENGTH_LONG).show();
            try {
                // Crea el archivo con Context para evitar excepcion de Android: SecurityException.
                ObjectOutputStream admin = new ObjectOutputStream(openFileOutput(fileName, Activity.MODE_PRIVATE));
                // Añade una HashMap con un usuario admin por defecto
                HashMap<String, Usuario> admin_user = new HashMap<>();
                admin_user.put("admin", new Usuario("admin", "admin", "admin", "admin", 'h'));
                admin.writeObject(admin_user);
                admin.close();
            } catch (IOException e) {
                // Muestra ventana emergente en la app con el mensaje de la excepcion
                Toast.makeText(this, "No se logro crear el archivo Archivos_Objetos/Usuarios.bixa", Toast.LENGTH_LONG).show();
            }
            // Lee los usuarios registrados en el archivo de objetos, que se usaran para la act. login y registro
        }

        try {
            ObjectInputStream baseDatos_usuarios = new ObjectInputStream(openFileInput(fileName));
            // Lee Hash Map de Usuarios
            UsuariosRegistrados = (HashMap<String,Usuario>) baseDatos_usuarios.readObject();
            baseDatos_usuarios.close();
        } catch (IOException | ClassNotFoundException e) {
            // Muestra ventana emergente en la app con el mensaje de la excepcion
            Toast.makeText(this,"Error al intentar leer la base de Usuarios, La app se cerrara",Toast.LENGTH_LONG).show();
            // Si hay un problema al leer el archivo, significa que este esta dañado y es necesario crearlo de nuevo
            base_datos.delete();
            finish();
        }


    }
}