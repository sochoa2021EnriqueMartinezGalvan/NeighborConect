package net.iessochoa.neighborconect.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.iessochoa.neighborconect.R;
import net.iessochoa.neighborconect.model.Mensaje;

public class ChatAdapter extends FirestoreRecyclerAdapter<Mensaje,ChatAdapter.ChatHolder> {

    private FirebaseAuth mAuth;
    private String usuario;



    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(@NonNull FirestoreRecyclerOptions<Mensaje> options) {
        super(options);


    }

    @Override
    protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Mensaje mensaje) {


//si el mensaje es del usuario lo colocamos a la izquierda

        if(mensaje.getUsuario().equals(usuario)){
            holder.tvCreadorMensaje.setText(mensaje.getUsuario());
            holder.tvMensaje.setText(mensaje.getBody());

            holder.cvOtroUsuario.setVisibility(View.GONE);
            holder.cvOtroMensaje.setVisibility(View.GONE);

        }else {
            holder.tvOtroCreador.setText(mensaje.getUsuario());
            holder.tvOtroMensaje.setText(mensaje.getBody());

            holder.cvUsuario.setVisibility(View.GONE);
            holder.cvMensaje.setVisibility(View.GONE);


        }
    }


    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new ChatHolder(view);
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        private TextView tvOtroMensaje, tvOtroCreador,tvCreadorMensaje,tvMensaje;
        private CardView cvUsuario, cvOtroUsuario,cvOtroMensaje,cvMensaje;


        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser userFB = mAuth.getCurrentUser();

            usuario= userFB.getEmail();
            tvOtroMensaje=itemView.findViewById(R.id.tvOtroMensaje);
            tvOtroCreador=itemView.findViewById(R.id.tvOtroCreador);
            tvCreadorMensaje = itemView.findViewById(R.id.tvCreadorMensaje);
            tvMensaje = itemView.findViewById(R.id.tvMensaje);
            cvOtroUsuario = itemView.findViewById(R.id.cvOtroUsuarioCreador);
            cvUsuario = itemView.findViewById(R.id.cvUsuarioCreador);
            cvMensaje = itemView.findViewById(R.id.cvMensaje);
            cvOtroMensaje = itemView.findViewById(R.id.cvOtroMensaje);



        }
    }
}