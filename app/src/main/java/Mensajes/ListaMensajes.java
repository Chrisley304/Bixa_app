package Mensajes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.login_plantilla.R;

import java.util.List;

/**
 * Esta clase utiliza una lista de mensajes para con ella, mostrar los mensajes en el Recycler View de
 * Bixa Main
 */
public class ListaMensajes extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_USUARIO = 1;
    private static final int VIEW_TYPE_BIXA = 2;

    private Context mContext;
    private List<Mensaje> lista_mens;

    /**
     * En el contructor de la clase se obtienen:
     * @param context el contexto de la actividad, nesesario para algunos metodos
     * @param messageList Es la lista de mensajes, que se va a mostrar, por ejemplo una ArrayList, pero contener objetos 'Mensaje'
     */
    public ListaMensajes(Context context, List<Mensaje> messageList) {
        mContext = context;
        lista_mens = messageList;
    }

    @Override
    public int getItemCount() {
        return lista_mens.size();
    }

    /**
     * Determina que tiempo de mensaje es (Mensaje de usuario o mensaje de bixa)
     * @param position posicion de la lista a analizar
     * @return retorna el tipo de mensaje que es (Bixa o Usuario)
     */
    @Override
    public int getItemViewType(int position) {
        Mensaje message = (Mensaje) lista_mens.get(position);

        // Si el emisor del "mensaje" no es Bixa, se utilizara el layout de mensaje de usuario
        if (!message.getEmisor().getUsername().equals("bixa")){
            return VIEW_TYPE_USUARIO;
        } else {
            // Si Bixa envio la respuesta a la peticion
            return VIEW_TYPE_BIXA;
        }
    }

    /**
     * Introduce los mensajes al holder
     * @param parent ViewGroup a utilizar
     * @param viewType El tipo de mensaje que es (Bixa o Usuario)
     * @return regresa el layout con el mensaje para ser "pegado" el recicler view
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_USUARIO) {
            // Si es mensaje del usuario
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.peticiones_usuario, parent, false);
            return new PeticionUsuarioHolder(view);
        } else if (viewType == VIEW_TYPE_BIXA) {
            // Si es respuesta de bixa
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.respuestas_bixa, parent, false);
            return new RespuestaBixaHolder(view);
        }

        return null;
    }

    /**
     * "Pega" los mensajes en la actividad
     * @param holder View holder donde se 'pegara' el mensaje
     * @param position Posicion del mensaje dentro de la lista
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Mensaje message = (Mensaje) lista_mens.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_USUARIO:
                ((PeticionUsuarioHolder) holder).bind(message);
                break;
            case VIEW_TYPE_BIXA:
                ((RespuestaBixaHolder) holder).bind(message);
        }
    }
}