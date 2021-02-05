package com.example.login_plantilla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import Excepciones_Bixa.UsernameNoPermitidoException;
import Excepciones_Bixa.UsuarioYaExistenteException;
import Usuarios.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Esta clase es la encargada de generar la actividad para editar el perfil de un usuario
 *
 * @author Christian Leyva, Fernanda Aguilar, Berenice Martinez
 */

public class EditarPerfil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Variables para controlar el menu despegable
    DrawerLayout dwly;
    Toolbar barra_herramientas;
    NavigationView navView;

    // Para la personalizacion de los menus
    CircleImageView BotonFotoperfil, fotopfDrawer;
    TextView nombreNavbar;
    TextView usernameNavbar;
    Usuario usuario_obj;
    String username_activity;

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
    private static final int CODIGO_PERMISO = 101;

    /**
     * En este metodo onCreate, se crean variables locales para generar el menu despegable, y conectarlo
     * con las otras 3 actividades que tambien incluyen este menu despegable.
     *
     * Tambien en este metodo, se coloca la informacion que ya se tiene del usuario en las casillas
     * EditText para que el usuario conozca la informacion que este ya coloco y solo modifique la que necesite.
     *
     * @param savedInstanceState Parametro recibido por defecto por las actividades para su creación.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        username_activity = getIntent().getStringExtra("Usuario");
        usuario_obj = BienvenidaActivity.UsuariosRegistrados.get(username_activity);
        String nombreCompleto = usuario_obj.getNombre() + " " + usuario_obj.getApellido();

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
        Edtx_nombres = findViewById(R.id.entrada_Nombres_editarinf);
        Edtx_apellidos = findViewById(R.id.entrada_apellidos_editarinf);
        Edtx_usuario = findViewById(R.id.entrada_usuario_editarinf);
        Edtx_contra1 = findViewById(R.id.entrada_txf_pass_1_editarinf);
        Edtx_contra2 = findViewById(R.id.entrada_txf_pass_2_editarinf);
        boton_Masc = findViewById(R.id.boton_masc_reg_editarinf);
        boton_Fem = findViewById(R.id.boton_fem_reg_editarinf);
        boton_Registrarse = findViewById(R.id.boton_editarinfo);
        FotoPerfilvista = findViewById(R.id.FotoPerfilVista_editarinf);

        /* Para el menu despegable:  ---------------------------------*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dwly, barra_herramientas, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dwly.addDrawerListener(toggle);
        toggle.onDrawerStateChanged(DrawerLayout.STATE_DRAGGING);
        toggle.syncState();
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);
        // Muestra como seleccionado por defecto la opcion de asistente del meu despegable
        navView.setCheckedItem(R.id.nav_editPerf);
        // Oculta opciones de administrador a personas no admin:
        if (!VerUsuariosRegistrados.EsAdmin(username_activity)){
            Menu menu = navView.getMenu();
            menu.findItem(R.id.nav_admin_Registros).setVisible(false);
        }

        // Agrega el nombre y username en el menu despegable
        nombreNavbar.setText(nombreCompleto);
        usernameNavbar.setText(username_activity);
        File imagen;
        // Si el usuario agrego foto de perfil, se colocara
        if (usuario_obj.getRuta_fotoperfil() != null) {
             imagen = new File(getFilesDir(), BienvenidaActivity.UsuariosRegistrados.get(username_activity).getRuta_fotoperfil());
            // Una vez con la imagen, se "comprime" para que sea mas ligero para el sistema moverla
            Bitmap bitmap_img = BitmapFactory.decodeFile(imagen.getPath());
            Bitmap imagen_comprimida = Bitmap.createScaledBitmap(bitmap_img, 128, 128, false);
            // Una vez comprimida, se coloca en los "marcos"
            BotonFotoperfil.setImageBitmap(imagen_comprimida);
            fotopfDrawer.setImageBitmap(imagen_comprimida);
            FotoPerfilvista.setImageDrawable(Drawable.createFromPath(imagen.toString()));

        }

        // Al estar actualizando informacion, se coloca la informacion previamente ingresada
        String nombres = usuario_obj.getNombre();
                Edtx_nombres.getEditText().setText(nombres);
        String apellidos = usuario_obj.getApellido();
                Edtx_apellidos.getEditText().setText(apellidos);
        String user = usuario_obj.getUsername();
        Edtx_usuario.getEditText().setText(user);
        String contra = usuario_obj.getContrasenia();
        Edtx_contra1.getEditText().setText(contra);
        char sexo = usuario_obj.getGenero();
        if (sexo == 'h'){
            boton_Masc.toggle();
        }else{
            boton_Fem.toggle();
        }


        BotonFotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditarPerfil.this, "Que buena foto " + usuario_obj.getNombre() + " !", Toast.LENGTH_SHORT).show();
            }
        });

        // Evento para selecionar la foto de perfil
        FotoPerfilvista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se "pide permiso" al usuario para utilizar imagenes de su galeria (Por cuestiones de privacidad de android)
                if(ContextCompat.checkSelfPermission(EditarPerfil.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    // Si no se tiene el permiso del usuario
                    ActivityCompat.requestPermissions(EditarPerfil.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODIGO_PERMISO);
                }
                else{
                    // Si se tiene el permiso se procede a cargar imagenes
                    cargarImagen();
                }
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

    /**
     * Este metodo es el necesario para registrar nuevos usuarios o en este caso en particular
     * de actualizar la informacion de usuarios ya existentes.
     * En este se crearon validaciones de datos y dependiendo el error del usario, se utilizaran las Excepciones
     * contenidas en el package Excepciones_Bixa
     * @param username Contiene el username ingresado por el usuario
     * @param contrasenia Contiene la contraseña ingresada por el usuario
     * @param contra2 Contiene la validacion de la contraseña ingresada por el usuario
     * @param nombre Contiene los nombres que ingreso el usuario
     * @param apellido Contiene los apellidos que ingreso el usuario
     * @param genero Caracter 'h' para hombre y 'm' para mujer obtenido de el grupo de botones circulares
     * @throws UsuarioYaExistenteException Excepcion lanzada en caso de que el Usuario ya se encuentre registrado en el sistema, al igual que si el usuario intenta crear un usuario con permisos de administrador, se
     * lanzara una UsernameNoPermitidoException, la cual hereda de esta excepcion.
     * @throws ContraseniaInseguraException Excepcion lanzada en caso de que el usuario haya ingresado una contraseña menor a 8 caracteres.
     * @throws ContraseniaIncorrectaException Excepcion lanzada en caso de que el usuario haya ingresado una contraseña diferente en la validacion de la misma
     * @throws CamposIncompletosException Excepcion lanzada en caso de que el usuario no haya llenado todos los campos de el formulario.
     */
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
        // Si el nombre de usuario se intento crear con permisos de administrador
        // Rechazara la entrada, ya que solo se pueden crear en el backend estos perfiles
        if (VerUsuariosRegistrados.EsAdmin(username)){
            // Solo se mandara esta alerta si el usuario que intento colocar un username de admin
            // no era admin antes de el intento
            if (!username.equals(username_activity)){
                throw new UsernameNoPermitidoException();
            }
        }

        // Se verifica que el nombre de usuario no se encuentre registrado actualmente o que en cuyo caso, sea el mismo que ingreso
        if(!BienvenidaActivity.UsuariosRegistrados.containsKey(username) || usuario_obj.getUsername().equals(username)){
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
                    // si todos los datos son correctos se borra el perfil biejo del usuario y  el nuevo  se añade a la hash Map
                    BienvenidaActivity.UsuariosRegistrados.remove(username);
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
                }
            }
        }else{
            throw new UsuarioYaExistenteException();
        }
    }

    /**
     * Metodo para utilizar imagenes de el telefono del usuario en la app
     */
    public void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicacion"),10);
    }

    /**
     * Este metodo obtiene el resultado que proporciona la ventana emergente de la seleccion de Imagenes
     * con la imagen seleccionada por el usuario.
     * @param requestCode parametro requerido por la funcion
     * @param resultCode parametro requerido por la funcion
     * @param data en este parametro se encuentra la imagen
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(resultCode == RESULT_OK){
            uri_imagenperfil = data.getData();
            FotoPerfilvista.setImageURI(uri_imagenperfil);
        }
    }

    /**
     *  Metodos para guardar imagenes que el usuario ingrese en el backend de la app, autor de estos metodos: haroon47
     * Repositorio de donde se obtuvo: https://github.com/haroon47/ImagesInternalStorage
     */

    private void insertInPrivateStorage(String name, String path) throws IOException {
        FileOutputStream fos  = openFileOutput(name,MODE_APPEND);

        File file = new File(path);

        byte[] bytes = getBytesFromFile(file);

        fos.write(bytes);
        fos.close();
    }

    /**
     * Convierte una intancia de File, en un arreglo de Bytes
     * @param file archivo a convertir
     * @return Regresa el arreglo de bytes generado a partir de la imagen
     * @throws IOException
     */
    private byte[] getBytesFromFile(File file) throws IOException {
        byte[] data = FileUtils.readFileToByteArray(file);
        return data;
    }

    /**
     *  Obtiene el nombre de la imagen a guardar
     * @param uri La imagen a obtener el nombre
     * @return Regresa el nombre de la imagen en String
     */
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

    /**
     * Obtiene la ruta REAL de la imagen obtenida, ya que los objetos Uri limitan la ruta y no permiten
     * trabajar correctamente con estos archivos
     * @param context Contiene el contexto de la actividad
     * @param uri Imagen a obtener la ruta
     * @return Regresa la ruta completa en String
     */
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

    /**
     * Este metodo sobre escribe lo que hace la aplicacion al presionar el boton de 'atras' de
     * el sistema android.
     * Si el menu despegable esta abierto, lo cerrara
     * Si no esta abierto, te mostrara una ventana emergente sobre si deseas cerrar sesion.
     */
    @Override
    public void onBackPressed() {
        if (dwly.isDrawerOpen(GravityCompat.START)) {
            dwly.closeDrawer(GravityCompat.START);
        } else {
            ClickCerrarSesion();
        }
    }

    /**
     * Este metodo crea una ventana emergente con un Builder, en este se muestra un mensaje sobre si estas
     * seguro que deseas cerrar sesion, y dependiendo de la respuesta del usuario, cerrara sesion, o ignorara el aviso.
     */
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

    /**
     * Cambio de actividad con el menu:
     * Hace que se quede la seleccion en el menu, para indicar en que parte de la app estas
     * @param item se refiere al que parte del menu seleccionaste.
     * @return regresa un boolean para indicar si funciono o no el metodo
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            // Caso editar perfil (Actividad Actual)
            case R.id.nav_editPerf: {
            }break;

            // Caso mas informacion
            case R.id.nav_bixa: {
                Intent bixa = new Intent(EditarPerfil.this, BixaMain.class);
                bixa.putExtra("Usuario", username_activity);
                startActivity(bixa);
                finish();
            }break;

            // Caso editar perfil
            case R.id.nav_about:{
                Intent edPerf = new Intent(EditarPerfil.this, SobrelaApp.class);
                edPerf.putExtra("Usuario", username_activity);
                startActivity(edPerf);
                finish();
            }break;

            // Caso registros admin:
            case R.id.nav_admin_Registros:{
                Intent admReg = new Intent(EditarPerfil.this, VerUsuariosRegistrados.class);
                admReg.putExtra("Usuario", username_activity);
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