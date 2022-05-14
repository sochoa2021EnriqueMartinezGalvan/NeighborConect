package net.iessochoa.neighborconect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import net.iessochoa.neighborconect.adapter.IncidenciasAdapter;
import net.iessochoa.neighborconect.model.Incidencias;

public class MisIncidencias extends AppCompatActivity {

    private RecyclerView rvMisincidencias;
    private IncidenciasAdapter adapter;
    private String idComunidad,email;
    private FirebaseAuth mAuth;
    private String numMensajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_incidencias);

        rvMisincidencias = findViewById(R.id.rvMisIncidencias);
        rvMisincidencias.setLayoutManager(new LinearLayoutManager(this));

        idComunidad = getIntent().getStringExtra("idComunidad");
        numMensajes = getIntent().getStringExtra("numMensajes");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFB = mAuth.getCurrentUser();

        email= userFB.getEmail();

        defineAdaptador();

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

            case R.id.action_com:
                Intent i = new Intent(this, Comunidad_Incidencias.class);
                i.putExtra("idComunidad", idComunidad);

                i.putExtra("numMensajes",numMensajes);
                startActivity(i);
                finish();
                return true;
            case R.id.action_chat:
                Intent i2 = new Intent(this, Chat.class);
                i2.putExtra("idComunidad", idComunidad);
                i2.putExtra("numMensajes",numMensajes);
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
                        Query.Direction.DESCENDING).whereEqualTo("usuario",email);
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
        rvMisincidencias.setAdapter(adapter);
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
                rvMisincidencias.smoothScrollToPosition(0);
            }

            @Override
            public void onDataChanged() {
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
            }
        });
    }

}