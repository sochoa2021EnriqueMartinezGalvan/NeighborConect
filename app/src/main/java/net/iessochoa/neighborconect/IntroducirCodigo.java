package net.iessochoa.neighborconect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.iessochoa.neighborconect.model.Comunidades;

import java.util.ArrayList;

public class IntroducirCodigo extends AppCompatActivity {

    private Button btInsertarCodigo;
    private EditText etInsertarCodigo;
    private ArrayList<Comunidades> listaComunidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introducir_codigo);
        btInsertarCodigo = findViewById(R.id.btInsertarCodigo);
        etInsertarCodigo = findViewById(R.id.etInsertarCodigo);

        btInsertarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leerComunidades();
                System.out.println(listaComunidades);
            }
        });

    }

    private void leerComunidades() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listaComunidades= new ArrayList<Comunidades>();
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
}