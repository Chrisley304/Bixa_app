package com.example.login_plantilla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.io.FileUtils;

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
    Uri uri_imagenperfil;

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
        navView = findViewById(R.id.nav_view);
        View barra_menu = navView.getHeaderView(0);
        fotopfDrawer = barra_menu.findViewById(R.id.FotoPerfilDrawer);
        nombreNavbar = barra_menu.findViewById(R.id.Nombre_navbar);
        usernameNavbar = barra_menu.findViewById(R.id.Username_navbar);
        barra_herramientas = findViewById(R.id.toolbar);

        //Editar info
        Edtx_nombres = findViewById(R.id.entrada_Nombres_registro);
        Edtx_apellidos = findViewById(R.id.entrada_apellidos_registro);
        Edtx_usuario = findViewById(R.id.entrada_usuario_registro);
        Edtx_contra1 = findViewById(R.id.entrada_txf_pass_1);
        Edtx_contra2 = findViewById(R.id.entrada_txf_pass_2);
        boton_Masc = findViewById(R.id.boton_masc_reg);
        boton_Fem = findViewById(R.id.boton_fem_reg);
        boton_Registrarse = findViewById(R.id.boton_registrarse);
        FotoPerfilvista = findViewById(R.id.FotoPerfilVista);

        /* Para el menu despegable:  ---------------------------------*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dwly, barra_herramientas, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    // Cierra el teclado al abrir el menu
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        };;
        dwly.addDrawerListener(toggle);
        toggle.onDrawerStateChanged(DrawerLayout.STATE_DRAGGING);
        toggle.syncState();
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);
        // Muestra como seleccionado por defecto la opcion de asistente del meu despegable
        navView.setCheckedItem(R.id.nav_editPerf);
        // Oculta opciones de administrador a personas no admin:
        if(!user.getUsername().equals("admin")){
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

        // Al estar actualizando informacion, se coloca la informacion previamente ingresada


        BotonFotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditarPerfil.this, "Que buena foto " + user.getNombre() + " !", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener de boton para actualizar informacion
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
                    Toast.makeText(EditarPerfil.this,"Por favor, ingresa los datos en todas casillas",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditarPerfil.this,"ERROR: No se logro actualizar la base de datos",Toast.LENGTH_LONG).show();
                    }
                    // Te envia a la pantalla del asistente
                    Intent intent  = new Intent(EditarPerfil.this, BixaMain.class);
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