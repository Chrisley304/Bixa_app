package com.example.login_plantilla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import Usuarios.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Variables para controlar el menu despegable
    DrawerLayout dwly;
    Toolbar barra_herramientas;
    NavigationView navView;

    // Para la personalizacion de los menus
    CircleImageView BotonFotoperfil, fotopfDrawer;
    TextView nombreNavbar;
    TextView usernameNavbar;
    Usuario user;
    String username;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        username = getIntent().getStringExtra("Usuario");
        user = BienvenidaActivity.UsuariosRegistrados.get(username);
        String nombreCompleto = user.getNombre() + " " + user.getApellido();

        // Navbar
        dwly = findViewById(R.id.DrawerLayout);
        BotonFotoperfil = findViewById(R.id.BotonNavDrawer);
//        fotopfDrawer = findViewById(R.id.FotoPerfilDrawer);
        nombreNavbar = findViewById(R.id.Nombre_navbar);
        usernameNavbar = findViewById(R.id.Username_navbar);
        barra_herramientas = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);

        /* Para el menu despegable:  ---------------------------------*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dwly, barra_herramientas, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dwly.addDrawerListener(toggle);
        toggle.syncState();
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);
        // Muestra como seleccionado por defecto la opcion de asistente del meu despegable
        navView.setCheckedItem(R.id.nav_editPerf);
        // Oculta opciones de administrador a personas no admin:
        if(!user.getUsername().equals("admin")){
            Menu menu = navView.getMenu();
            menu.getItem(R.id.nav_admin_Registros).setVisible(false);
        }
        /*nombreNavbar.setText(nombreCompleto);
        usernameNavbar.setText(username);*/
        // Si el usuario agrego foto de perfil, se colocara

        if (user.getRuta_fotoperfil() != null) {
            BotonFotoperfil.setImageURI(Uri.fromFile(user.getRuta_fotoperfil()));
            fotopfDrawer.setImageURI(Uri.fromFile(user.getRuta_fotoperfil()));
        }

    }

    @Override
    public void onBackPressed() {
        if (dwly.isDrawerOpen(GravityCompat.START)) {
            dwly.closeDrawer(GravityCompat.START);
        } else {
            ClickCerrarSesion();
        }
    }

    public void ClickCerrarSesion() {
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

    //Cambio de actividad con el menu:
    // Hace que se quede la seleccion en el menu, para indicar en que parte de la app estas
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            // Caso asistente (Actividad Actual)
            case R.id.nav_editPerf: {
            }break;

            // Caso mas informacion
            case R.id.nav_bixa: {
                Intent bixa = new Intent(EditarPerfil.this, BixaMain.class);
                bixa.putExtra("Usuario",username);
                startActivity(bixa);
                finish();
            }break;

            // Caso editar perfil
            case R.id.nav_about:{
                Intent edPerf = new Intent(EditarPerfil.this, EditarPerfil.class);
                edPerf.putExtra("Usuario",username);
                startActivity(edPerf);
                finish();
            }break;

            // Caso cerrar sesion:
            case R.id.nav_logout:{
                ClickCerrarSesion();
            }
        }
        // CIerra el menu despegable al seleccionar alguna opcion
        dwly.closeDrawer(GravityCompat.START);
        return true;
    }
}