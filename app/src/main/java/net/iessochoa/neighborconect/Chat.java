package net.iessochoa.neighborconect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.iessochoa.neighborconect.adapter.ChatAdapter;
import net.iessochoa.neighborconect.model.Comunidades;
import net.iessochoa.neighborconect.model.Mensaje;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    private String idComunidad, numMensajes, usuario;
    private EditText etMensaje;
    private Button btEnviarMensaje;

    private FirebaseAuth mAuth;

    private RecyclerView rvChat;
    private ChatAdapter adapter;

    private int a = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFB = mAuth.getCurrentUser();

        idComunidad = getIntent().getStringExtra("idComunidad");

        numMensajes = getIntent().getStringExtra("numMensajes");

        usuario = userFB.getEmail();
        rvChat = findViewById(R.id.rvChat);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setNestedScrollingEnabled(false);


        try {
            defineAdaptador();
        } catch (Exception ex) {
            System.out.println(ex.getCause());
        }

        etMensaje = findViewById(R.id.etMensaje);
        btEnviarMensaje = findViewById(R.id.btEnviarMensaje);

        btEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje();
                defineAdaptador();

            }
        });




    }


    private void enviarMensaje() {
        String body = etMensaje.getText().toString();
        if (!body.isEmpty()) {
//usuario y mensaje
            Mensaje mensaje = new Mensaje(usuario, body);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Comunidades")
//documento conferencia actual
                    .document(idComunidad)
//subcolección de la conferencia
                    .collection("Chat")
//añadimos el mensaje nuevo
                    .add(mensaje);
            etMensaje.setText("");
            ocultarTeclado();
        }
    }

    private void ocultarTeclado() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etMensaje.getWindowToken(), 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is  present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_comunidad, menu);
        return true;
    }

    private void defineAdaptador() {
//consulta en Firebase
        Query query = FirebaseFirestore.getInstance()
//coleccion conferencias
                .collection("Comunidades")
//documento: conferencia actual
                .document(idComunidad)
//colección chat de la conferencia
                .collection("Chat")
//obtenemos la lista ordenada por fecha
                .orderBy("fechaCreacion",
                        Query.Direction.ASCENDING);
//Creamos la opciones del FirebaseAdapter
        FirestoreRecyclerOptions<Mensaje> options = new
                FirestoreRecyclerOptions.Builder<Mensaje>()
//consulta y clase en la que se guarda los datos
                .setQuery(query, Mensaje.class)
                .setLifecycleOwner(this)
                .build();
//si el usuario ya habia seleccionado otra conferencia, paramos las        escucha
        if (adapter != null) {
            adapter.stopListening();
        }
//Creamos el adaptador
        adapter = new ChatAdapter(options);
//asignamos el adaptador
        rvChat.setAdapter(adapter);
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
                /*int n=1;
                try {
                   n= Integer.parseInt(numMensajes);
                    a += 1;

                    rvChat.smoothScrollToPosition(n-1);
                }catch (Exception e){

                }*/


            }

            @Override
            public void onDataChanged() {

            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
            }
        });
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

            case R.id.action_com:
                Intent i2 = new Intent(this, Comunidad_Incidencias.class);
                i2.putExtra("idComunidad", idComunidad);
                i2.putExtra("numMensajes", numMensajes);
                startActivity(i2);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}