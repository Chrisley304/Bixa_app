package Mensajes;

import android.icu.text.SimpleDateFormat;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.login_plantilla.R;

import java.util.Locale;
/**
 * Esta clase crea el layout de los mensajes de BIXA que se van a pegar dentro del recycler view
 */
public class RespuestaBixaHolder extends RecyclerView.ViewHolder {
    TextView texto_mensaje, texto_hora;

    RespuestaBixaHolder(View itemView) {
        super(itemView);
        texto_mensaje = (TextView) itemView.findViewById(R.id.Textvw_MensajeBixa);
        texto_hora = (TextView) itemView.findViewById(R.id.Textvw_HoraMenBixa);
    }
    /**
     *  Aqui se coloca la informacion del objeto Mensaje en los textview del mensaje
     * @param mensaje Mensaje a utilizar
     */
    void bind(Mensaje mensaje) {
        texto_mensaje.setText(mensaje.getMensaje());
        // Obtiene la hora de creacion mediante SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        texto_hora.setText(dateFormat.format(mensaje.getTiempo_envio()));
    }
}