package net.iessochoa.neighborconect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.iessochoa.neighborconect.model.Incidencias;

import java.util.HashMap;
import java.util.Map;

public class Crear_Incidencias extends AppCompatActivity {


    private EditText etDescrpcion;
    private Button btGuardar;
    private String idComunidad,email;
    private Map<String, String> usr = new HashMap<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencias);


        idComunidad = getIntent().getStringExtra("idComunidad");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFB = mAuth.getCurrentUser();

        email = userFB.getEmail();

        etDescrpcion=findViewById(R.id.etDescrpcion);
        btGuardar=findViewById(R.id.btGuardarIncidencia);
        leerUsurarios();

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUsuario=buscarUsuario();
                crearIncidencia(idUsuario);
            }
        });


        }

    private String buscarUsuario() {

        String idUsuario= usr.get(email);

        return idUsuario;
    }


    private void crearIncidencia(String idUsuario) {

        Incidencias in = new Incidencias(email,etDescrpcion.getText().toString());


        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Add a new document with a generated ID
        db.collection("Comunidades").document(idComunidad).collection("Incidencias").add(in)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


        finish();

    }

    private void leerUsurarios() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("Comunidades").document(idComunidad).collection("Usuarios_Comunidad_"+idComunidad)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " +
                                        document.getData());


                                usr.put(document.getData().get("email").toString(), document.getId());
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ",
                                    task.getException());
                        }
                    }
                });


    }
}