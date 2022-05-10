package net.iessochoa.neighborconect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import net.iessochoa.neighborconect.model.Usuarios;

public class Comunidad_Incidencias extends AppCompatActivity {

    private FloatingActionButton fabCrearIncidencia;
    private String idComunidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunidad_incidencias);


        idComunidad = getIntent().getStringExtra("idComunidad");

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


}