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
import android.widget.Toast;

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

import net.iessochoa.neighborconect.model.Comunidades;
import net.iessochoa.neighborconect.model.Usuarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IntroducirCodigo extends AppCompatActivity {

    private Button btInsertarCodigo;
    private EditText etInsertarCodigo;
    private ArrayList<Comunidades> listaComunidades;
    private Map<String, String> doc = new HashMap<>();
    private Map<String, String> usr = new HashMap<>();

    private String email, code, documentId,user_email=null;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introducir_codigo);
        btInsertarCodigo = findViewById(R.id.btInsertarCodigo);
        etInsertarCodigo = findViewById(R.id.etInsertarCodigo);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFB = mAuth.getCurrentUser();

        email = userFB.getEmail();

        leerComunidades();
       // leerUsurarios();

        btInsertarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                code = etInsertarCodigo.getText().toString();
                documentId = doc.get(code);


                boolean existe = false;
                for (Comunidades comunidad : listaComunidades) {
                    if (comunidad.getCode().equals(code)) {
                        existe = true;
                    }
                }

                if (existe) {
                    Toast.makeText(getApplicationContext(), "Entrando a la comunidad", Toast.LENGTH_SHORT).show();

                   // leerUsurarios();
                   // user_email=usr.get(email);
                   // if (user_email!=null){
                      //  entrarComunidad();
                   // }


                    Intent i = new Intent(IntroducirCodigo.this, Comunidad_Incidencias.class);
                    i.putExtra("idComunidad", documentId);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "El codigo no existe", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    private void entrarComunidad() {


        Usuarios usr = new Usuarios(email);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Add a new document with a generated ID
        db.collection("Comunidades").document(documentId).collection("Usuarios_Comunidad_"+documentId).add(usr)
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


    private void leerComunidades() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listaComunidades = new ArrayList<Comunidades>();

        db.collection("Comunidades")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " +
                                        document.getData());

                                listaComunidades.add(document.toObject(Comunidades.class));
                                doc.put(document.getData().get("code").toString(), document.getId());
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ",
                                    task.getException());
                        }
                    }
                });

    }
    private void leerUsurarios() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("Comunidades").document(documentId).collection("Usuarios_Comunidad_"+documentId)
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