package Mensajes;

import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.login_plantilla.R;

import java.util.Locale;

import Usuarios.Usuario;

public class PeticionUsuarioHolder extends RecyclerView.ViewHolder {
    TextView mensajeUsua, horaUsuario , nombreusuario_mensaje;
    ImageView fotoPerfilUs;

    PeticionUsuarioHolder(View itemView) {
        super(itemView);

        mensajeUsua = (TextView) itemView.findViewById(R.id.Textvw_MensajeUsuario);
        horaUsuario = (TextView) itemView.findViewById(R.id.Textvw_HoraMenUsuario);
        nombreusuario_mensaje = (TextView) itemView.findViewById(R.id.NombreUsuario);
        fotoPerfilUs = (ImageView) itemView.findViewById(R.id.ImagenUsuario_perfil_petUs);
    }

    void bind(Mensaje mensaje) {
        // Inseta el mensaje en la plantilla de mensaje
        mensajeUsua.setText(mensaje.getMensaje());
        // COloca el nombre del emisor arriba del mensaje
        nombreusuario_mensaje.setText(mensaje.getEmisor().getNombre());
        // Crea el formato de la hora del usuario
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        horaUsuario.setText(dateFormat.format(mensaje.getTiempo_envio()));
        // Inserta la foto de perfil que el usuario Ingreso

//        fotoPerfilUs.setImageURI(Uri.fromFile(mensaje.getEmisor().getRuta_fotoperfil()));

    }
}