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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Usuarios.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class VerUsuariosRegistrados extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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


    // Para colocar la informacion en los TextViews
    TextView txtvw_contadmin;
    TextView txtvw_contusers;
    TextView txtvw_admins;
    TextView txtvw_users;
    EditText editText_eliminar;
    Button boton_eliminar;

    ArrayList<Usuario> administradores;
    ArrayList<Usuario> usuariosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios_registrados);

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
        StringTokenizer stk = new StringTokenizer(username,"_");
        while (stk.hasMoreTokens()){
            String token = stk.nextToken();
            if (token.equals("admin")){
                Menu menu = navView.getMenu();
                menu.findItem(R.id.nav_admin_Registros).setVisible(false);
            }
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
        BotonFotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerUsuariosRegistrados.this, "Que buena foto " + user.getNombre() + " !", Toast.LENGTH_SHORT).show();
            }
        });

        txtvw_contadmin = findViewById(R.id.contadorAdmin);
        txtvw_contusers = findViewById(R.id.contadorUsuarios);
        txtvw_admins = findViewById(R.id.textview_admins);
        txtvw_users = findViewById(R.id.textview_usuarios);
        editText_eliminar = findViewById(R.id.editTextUsuariodel);
        boton_eliminar = findViewById(R.id.BotonUsuariodel);

        administradores = new ArrayList<>();
        usuariosList = new ArrayList<>();

        CargarUsuarios();

        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usaEl = editText_eliminar.getText().toString();
                if (usaEl.isEmpty()){
                    Toast.makeText(VerUsuariosRegistrados.this,"Por favor, ingresa el nombre de usuario a eliminar",Toast.LENGTH_SHORT).show();
                }else {
                    Usuario delUs = BienvenidaActivity.UsuariosRegistrados.get(usaEl);
                    if (!administradores.contains(delUs)){
                        if (usuariosList.contains(delUs)){
                            BienvenidaActivity.UsuariosRegistrados.remove(delUs);
                            CargarUsuarios();
                        }else{
                            Toast.makeText(VerUsuariosRegistrados.this,"El usuario no fue encontrado :c",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(VerUsuariosRegistrados.this,"No puedes eliminar a un administrador",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void CargarUsuarios(){
        administradores.clear();
        usuariosList.clear();
        for (String i: BienvenidaActivity.UsuariosRegistrados.keySet()) {
            boolean eraAdmin = false;
            StringTokenizer stk = new StringTokenizer(i,"_");
            while (stk.hasMoreTokens()){
                String token = stk.nextToken();
                if (token.equals("admin")){
                    administradores.add(BienvenidaActivity.UsuariosRegistrados.get(i));
                    eraAdmin = true;
                }
            }
            if (!eraAdmin){
                usuariosList.add(BienvenidaActivity.UsuariosRegistrados.get(i));
            }
        }
        String admins_string = "";
        for(Usuario admin : administradores){
            admins_string += admin.getUsername() + " -> " + admin.getNombre() + " " + admin.getApellido() + "\n";
        }

        String users_string = "";
        for(Usuario userss : usuariosList){
            users_string += userss.getUsername() + " -> " + userss.getNombre() + " " + userss.getApellido() + "\n";
        }

        // Finalmente se coloca en la actividad

        txtvw_contadmin.setText(administradores.size());
        txtvw_contusers.setText(usuariosList.size());
        txtvw_admins.setText(admins_string);
        txtvw_users.setText(users_string);
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
                Intent bixa = new Intent(VerUsuariosRegistrados.this, BixaMain.class);
                bixa.putExtra("Usuario",username);
                startActivity(bixa);
                finish();
            }break;

            // Caso editar perfil
            case R.id.nav_editPerf:{
                Intent edPerf = new Intent(VerUsuariosRegistrados.this, EditarPerfil.class);
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