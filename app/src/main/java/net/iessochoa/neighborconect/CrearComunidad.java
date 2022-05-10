package net.iessochoa.neighborconect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import net.iessochoa.neighborconect.model.Comunidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CrearComunidad extends AppCompatActivity {

    private Button btCrearComunidad;

    private EditText etCode, etName;


    private ArrayList<Comunidades> listaComunidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_comunidad);



        btCrearComunidad = findViewById(R.id.btCrearComunidad);
        etCode = findViewById(R.id.etCode);
        etName = findViewById(R.id.etName);

        leerComunidades();

        btCrearComunidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarTeclado();

                boolean existe = false;
                String nombre = etName.getText().toString();
                String code = etCode.getText().toString();

                for (Comunidades comunidad : listaComunidades) {
                    if (comunidad.getCode().equals(code)) {
                        existe = true;
                    }
                }
                System.out.println(existe);
                if (existe) {
                    Toast.makeText(getApplicationContext(), "El codigo ya esta en uso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Comunidad Creada", Toast.LENGTH_SHORT).show();
                    insertarDatos(code, nombre);
                }

            }
        });

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
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ",
                                    task.getException());
                        }
                    }
                });

    }
    private void insertarDatos(String code, String nombre) {
        //Codigo para insertar comunidades
        Comunidades comunidad = new Comunidades(code, nombre);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Add a new document with a generated ID
        db.collection("Comunidades")
                .add(comunidad)
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

    private void ocultarTeclado() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etCode.getWindowToken(), 0);
        }
    }


}