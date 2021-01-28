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

public class ListaMensajes extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_USUARIO = 1;
    private static final int VIEW_TYPE_BIXA = 2;

    private Context mContext;
    private List<Mensaje> lista_mens;

    public ListaMensajes(Context context, List<Mensaje> messageList) {
        mContext = context;
        lista_mens = messageList;
    }

    @Override
    public int getItemCount() {
        return lista_mens.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
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

    // Introduce los mensajes al holder
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

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
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