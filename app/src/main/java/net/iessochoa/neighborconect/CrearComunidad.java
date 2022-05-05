package net.iessochoa.neighborconect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import net.iessochoa.neighborconect.model.Comunidades;

import java.util.HashMap;
import java.util.Map;

public class CrearComunidad extends AppCompatActivity {

    private Button btCrearComunidad;

    private EditText etCode, etName;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_comunidad);

        auth = FirebaseAuth.getInstance();

        btCrearComunidad = findViewById(R.id.btCrearComunidad);
        etCode = findViewById(R.id.etCode);
        etName = findViewById(R.id.etName);

        btCrearComunidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarDatos();
            }
        });

    }

    private void insertarDatos() {
        //Codigo para insertar comunidades
        String nombre = etName.getText().toString();
        String code = etCode.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> comunidad = new HashMap<>();
        comunidad.put("codigo", code);
        comunidad.put("nombre", nombre);


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


}