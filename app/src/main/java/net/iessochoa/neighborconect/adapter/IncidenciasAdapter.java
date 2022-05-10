package net.iessochoa.neighborconect.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import net.iessochoa.neighborconect.R;
import net.iessochoa.neighborconect.model.Incidencias;

public class IncidenciasAdapter extends FirestoreRecyclerAdapter<Incidencias, IncidenciasAdapter.IncidenciasHolder> {

    private String descripcion,usuario,fecha;

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
        holder.tvDescripcion.setText(incidencias.getDescripcion());
        holder.tvcreador.setText(incidencias.getUsuario());
        try{
            holder.tvFecha.setText(incidencias.getFechaCreacion().toString());
        }catch (Exception ex){
            System.out.println("Error");
        }

        /*descripcion=incidencias.getDescripcion();
        usuario=incidencias.getUsuario();
        fecha=incidencias.getFechaCreacion().toString();*/

    }


    @NonNull
    @Override
    public IncidenciasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.incidencias_item, parent, false);
        return new IncidenciasHolder(view);
    }

    public class IncidenciasHolder extends RecyclerView.ViewHolder {
        private TextView tvDescripcion,tvcreador,tvFecha;
        private CardView cvContenedor;

        public IncidenciasHolder(@NonNull View itemView) {
            super(itemView);

            tvDescripcion=itemView.findViewById(R.id.tvDescripcion);
            cvContenedor=itemView.findViewById(R.id.cvContenedor);
            tvcreador=itemView.findViewById(R.id.tvCreador);
            tvFecha=itemView.findViewById(R.id.tvFecha);

            cvContenedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Creador: "+tvcreador.getText().toString());
                    builder.setMessage("Fecha: "+tvFecha.getText().toString()+"\n"+tvDescripcion.getText().toString());
                    builder.setPositiveButton("Aceptar", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    //Toast.makeText(v.getContext(), descripcion, Toast.LENGTH_SHORT).show();
                }
            });



        }



    }


}
