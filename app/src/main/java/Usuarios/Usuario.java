package Usuarios;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;

/**
 * Clase en la cual se guardan distintos tipos de datos primitivos, para en conjunto crear el perfil
 * de un usuario.
 * Esta clase implementa Serializable, para que esta se pueda almacenar en un archivo de objetos
 */
public class Usuario implements Serializable {
    private String username;
    private String contrasenia;
    private String nombre;
    private String apellido;
    private char genero;
    String ruta_fotoperfil;

    // Ocupacion
    public Usuario(String username, String contrasenia, String nombre, String apellido, char genero) {
        this.username = username;
        this.contrasenia = contrasenia;
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    public void setRuta_fotoperfil(String ruta_fotoperfil) {
        this.ruta_fotoperfil = ruta_fotoperfil;
    }

    public char getGenero() {
        return genero;
    }

    public String getRuta_fotoperfil() {
        return ruta_fotoperfil;
    }

    public String getUsername() {
        return username;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
}
