package net.iessochoa.neighborconect.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import net.iessochoa.neighborconect.R;
import net.iessochoa.neighborconect.model.Comunidades;
import net.iessochoa.neighborconect.model.Incidencias;

public class ComunidadAdapter extends FirestoreRecyclerAdapter<Comunidades, ComunidadAdapter.ComunidadesHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ComunidadAdapter(@NonNull FirestoreRecyclerOptions<Comunidades> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull ComunidadesHolder holder, int position, @NonNull Comunidades comunidades) {

        holder.tvNombre.setText("Nombre: "+comunidades.getName());
        holder.tvCode.setText("Codigo: "+comunidades.getCode());


    }


    @NonNull
    @Override
    public ComunidadesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comunidad, parent, false);
        return new ComunidadesHolder(view);
    }

    public class ComunidadesHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvCode;



        public ComunidadesHolder(@NonNull View itemView) {
            super(itemView);

            tvCode = itemView.findViewById(R.id.tvCodigoComunidad);
            tvNombre = itemView.findViewById(R.id.tvNombreComunidad);



        }
    }
}