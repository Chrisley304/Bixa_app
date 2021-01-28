package Mensajes;

import Usuarios.Usuario;

public class Mensaje {
    private String mensaje;
    private Usuario emisor;
    // Tiempo en milisegundos para convertirlo en Horas:minutos mas tarde
    private long tiempo_envio;

    public Mensaje(String mensaje, Usuario emisor, long tiempo_envio) {
        this.mensaje = mensaje;
        this.emisor = emisor;
        this.tiempo_envio = tiempo_envio;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public long getTiempo_envio() {
        return tiempo_envio;
    }
}
