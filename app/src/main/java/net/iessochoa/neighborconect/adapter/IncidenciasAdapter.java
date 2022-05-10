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

import net.iessochoa.neighborconect.R;
import net.iessochoa.neighborconect.model.Incidencias;

public class IncidenciasAdapter extends FirestoreRecyclerAdapter<Incidencias, IncidenciasAdapter.IncidenciasHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public IncidenciasAdapter(@NonNull FirestoreRecyclerOptions<Incidencias> options) {
        super(options);


    }

    @Override
    protected void onBindViewHolder(@NonNull IncidenciasHolder holder, int position, @NonNull Incidencias incidencias) {
       // holder.tvMensaje.setText(mensaje.getUsuario() + "=>" + mensaje.getBody());
//si el mensaje es del usuario lo colocamos a la izquierda

        /*if(mensaje.getUsuario().equals(usuario)){
            holder.cvContenedor.setCardBackgroundColor(Color.YELLOW);
            holder.tvMensaje.setGravity(Gravity.LEFT);
        }else {
            holder.tvMensaje.setGravity(Gravity.RIGHT);
            holder.cvContenedor.setCardBackgroundColor(Color.WHITE);
        }*/
    }


    @NonNull
    @Override
    public IncidenciasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.incidencias_item, parent, false);
        return new IncidenciasHolder(view);
    }

    public class IncidenciasHolder extends RecyclerView.ViewHolder {
        private TextView tvDescripcion;
        private CardView cvContenedor;




        public IncidenciasHolder(@NonNull View itemView) {
            super(itemView);

            tvDescripcion=itemView.findViewById(R.id.tvDescripcion);
            cvContenedor=itemView.findViewById(R.id.cvContenedor);


        }
    }


}
