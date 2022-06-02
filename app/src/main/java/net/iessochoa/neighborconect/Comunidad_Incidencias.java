package net.iessochoa.neighborconect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.iessochoa.neighborconect.adapter.ComunidadAdapter;
import net.iessochoa.neighborconect.adapter.IncidenciasAdapter;
import net.iessochoa.neighborconect.model.Incidencias;
import net.iessochoa.neighborconect.model.Usuarios;

public class Comunidad_Incidencias extends AppCompatActivity {

    private FloatingActionButton fabCrearIncidencia;
    private String idComunidad;


    int numMensajes = 0;


    RecyclerView rvIncidencias;
    IncidenciasAdapter adapter;
    ComunidadAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunidad_incidencias);

        //RecyclerView
        rvIncidencias = findViewById(R.id.rvIncidencias);
        rvIncidencias.setLayoutManager(new LinearLayoutManager(this));



        idComunidad = getIntent().getStringExtra("idComunidad");

        defineAdaptador();
        obtenerNumeroDeMensajes();

        fabCrearIncidencia = findViewById(R.id.fabAddIncidencias);

        fabCrearIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Crear_Incidencias.class);
                i.putExtra("idComunidad", idComunidad);

                startActivity(i);

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is  present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_comunidad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_misIncidencias:
                Intent i = new Intent(this, MisIncidencias.class);
                i.putExtra("idComunidad", idComunidad);
                i.putExtra("numMensajes", numMensajes);
                startActivity(i);
                finish();
                return true;
            case R.id.action_chat:
                Intent i2 = new Intent(this, Chat.class);
                i2.putExtra("idComunidad", idComunidad);
                String n = String.valueOf(numMensajes);
                i2.putExtra("numMensajes", n);

                startActivity(i2);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
                obtenerdatos();
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
            }
        });
    }

    public void obtenerdatos(){
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

    private void obtenerNumeroDeMensajes() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("Comunidades").document(idComunidad).collection("Chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " +
                                        document.getData());
                                numMensajes += 1;

                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ",
                                    task.getException());
                        }
                    }
                });
    }

    //es necesario parar la escucha
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}