package com.example.login_plantilla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.StringTokenizer;

import Usuarios.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class SobrelaApp extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobrela_app);

        username = getIntent().getStringExtra("Usuario");
        user = BienvenidaActivity.UsuariosRegistrados.get(username);
        String nombreCompleto = user.getNombre() + " " + user.getApellido();

        // Navbar
        dwly = findViewById(R.id.DrawerLayout);
        BotonFotoperfil = findViewById(R.id.BotonNavDrawer);
        navView = findViewById(R.id.nav_view);
        View barra_menu = navView.getHeaderView(0);
        fotopfDrawer = barra_menu.findViewById(R.id.FotoPerfilDrawer);
        nombreNavbar = barra_menu.findViewById(R.id.Nombre_navbar);
        usernameNavbar = barra_menu.findViewById(R.id.Username_navbar);
        barra_herramientas = findViewById(R.id.toolbar);

        /* Para el menu despegable:  ---------------------------------*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dwly, barra_herramientas, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dwly.addDrawerListener(toggle);
        toggle.onDrawerStateChanged(DrawerLayout.STATE_DRAGGING);
        toggle.syncState();
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);
        // Muestra como seleccionado por defecto la opcion de asistente del meu despegable
        navView.setCheckedItem(R.id.nav_about);
        // Oculta opciones de administrador a personas no admin:
        if (!VerUsuariosRegistrados.EsAdmin(username)){
            Menu menu = navView.getMenu();
            menu.findItem(R.id.nav_admin_Registros).setVisible(false);
        }

        // Agrega el nombre y username en el menu despegable
        nombreNavbar.setText(nombreCompleto);
        usernameNavbar.setText(username);

        // Si el usuario agrego foto de perfil, se colocara
        if (user.getRuta_fotoperfil() != null) {
            File imagen = new File(getFilesDir(), BienvenidaActivity.UsuariosRegistrados.get(username).getRuta_fotoperfil());
            // Una vez con la imagen, se "comprime" para que sea mas ligero para el sistema moverla
            Bitmap bitmap_img = BitmapFactory.decodeFile(imagen.getPath());
            Bitmap imagen_comprimida = Bitmap.createScaledBitmap(bitmap_img, 128, 128, false);
            // Una vez comprimida, se coloca en los "marcos"
            BotonFotoperfil.setImageBitmap(imagen_comprimida);
//            BotonFotoperfil.setImageDrawable(Drawable.createFromPath(imagen.toString()));
            fotopfDrawer.setImageBitmap(imagen_comprimida);
        }
        TextView linkChris = findViewById(R.id.ChrisLink);
        linkChris.setMovementMethod(LinkMovementMethod.getInstance());
        BotonFotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SobrelaApp.this, "Que buena foto " + user.getNombre() + " !", Toast.LENGTH_SHORT).show();
            }
        });
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
            case R.id.nav_about: {
            }break;

            // Caso mas informacion
            case R.id.nav_bixa: {
                Intent bixa = new Intent(SobrelaApp.this, BixaMain.class);
                bixa.putExtra("Usuario",username);
                startActivity(bixa);
                finish();
            }break;

            // Caso editar perfil
            case R.id.nav_editPerf:{
                Intent edPerf = new Intent(SobrelaApp.this, EditarPerfil.class);
                edPerf.putExtra("Usuario",username);
                startActivity(edPerf);
                finish();
            }break;

            // Caso registros admin:
            case R.id.nav_admin_Registros:{
                Intent admReg = new Intent(SobrelaApp.this, VerUsuariosRegistrados.class);
                admReg.putExtra("Usuario", username);
                startActivity(admReg);
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