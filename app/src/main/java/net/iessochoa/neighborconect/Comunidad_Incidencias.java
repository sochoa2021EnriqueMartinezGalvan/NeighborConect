package net.iessochoa.neighborconect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import net.iessochoa.neighborconect.adapter.IncidenciasAdapter;
import net.iessochoa.neighborconect.model.Incidencias;
import net.iessochoa.neighborconect.model.Usuarios;

public class Comunidad_Incidencias extends AppCompatActivity {

    private FloatingActionButton fabCrearIncidencia;
    private String idComunidad;

    RecyclerView rvIncidencias;
    IncidenciasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunidad_incidencias);

        //RecyclerView
        rvIncidencias = findViewById(R.id.rvIncidencias);
        rvIncidencias.setLayoutManager(new LinearLayoutManager(this));


        idComunidad = getIntent().getStringExtra("idComunidad");

        defineAdaptador();

        fabCrearIncidencia=findViewById(R.id.fabAddIncidencias);

        fabCrearIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Crear_Incidencias.class);
                i.putExtra("idComunidad",idComunidad);
                startActivity(i);

            }
        });
    }

    private void defineAdaptador() {
//consulta en Firebase
        Query query = FirebaseFirestore.getInstance()
//coleccion conferencias
                .collection("Comunidades")
//documento: conferencia actual
                .document(idComunidad)
//colección chat de la conferencia
                .collection("Incidencias")
//obtenemos la lista ordenada por fecha
                .orderBy("fechaCreacion",
                        Query.Direction.DESCENDING);
//Creamos la opciones del FirebaseAdapter
        FirestoreRecyclerOptions<Incidencias> options = new
                FirestoreRecyclerOptions.Builder<Incidencias>()
//consulta y clase en la que se guarda los datos
                .setQuery(query, Incidencias.class)
                .setLifecycleOwner(this)
                .build();
//si el usuario ya habia seleccionado otra conferencia, paramos las        escucha
        if (adapter != null) {
            adapter.stopListening();
        }
//Creamos el adaptador
        adapter = new IncidenciasAdapter(options);
//asignamos el adaptador
        rvIncidencias.setAdapter(adapter);
//comenzamos a escuchar. Normalmente solo tenemos un adaptador, esto  tenemos que
        //hacerlo en el evento onStar, como indica la documentación
        adapter.startListening();
//Podemos reaccionar ante cambios en la query( se añade un mesaje).Nosotros,
        // //lo que necesitamos es mover el scroll
        // del recyclerView al inicio para ver el mensaje nuevo
        adapter.getSnapshots().addChangeEventListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull
                    DocumentSnapshot snapshot, int newIndex, int oldIndex) {
                rvIncidencias.smoothScrollToPosition(0);
            }

            @Override
            public void onDataChanged() {
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
            }
        });
    }

    //es necesario parar la escucha
    /*@Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }*/


}