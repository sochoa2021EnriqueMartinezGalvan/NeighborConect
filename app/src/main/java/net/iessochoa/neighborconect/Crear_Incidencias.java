package net.iessochoa.neighborconect;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.iessochoa.neighborconect.model.Incidencias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class Crear_Incidencias extends AppCompatActivity {


    private EditText etDescrpcion;
    private Button btGuardar, btFoto;
    private String idComunidad, email;
    private Map<String, String> usr = new HashMap<>();
    private FirebaseAuth mAuth;
    private ConstraintLayout clIncidencias;
    private ImageView ivFoto;

    private Uri uriFoto=null;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private static final int MY_PERMISSIONS = 100;
    public final static int STATUS_CODE_SELECTION_IMAGEN = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencias);

        clIncidencias = findViewById(R.id.clIncidencias);
        idComunidad = getIntent().getStringExtra("idComunidad");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFB = mAuth.getCurrentUser();

        email = userFB.getEmail();

        etDescrpcion = findViewById(R.id.etDescrpcion);
        btGuardar = findViewById(R.id.btGuardarIncidencia);
        btFoto = findViewById(R.id.btFoto);
        ivFoto= findViewById(R.id.ivFoto);
        leerUsurarios();

        btFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarTeclado();

                muestraOpcionesImagen();

            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUsuario = buscarUsuario();
                crearIncidencia(idUsuario);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case STATUS_CODE_SELECTION_IMAGEN:
                    uriFoto = data.getData();
                    muestraFoto();
                    break;
            }
        }
    }


    private void muestraFoto() {
        Glide.with(this)
                .load(uriFoto) // Uri of the picture
                .into(ivFoto);//imageView
    }

    private void muestraOpcionesImagen() {
        final CharSequence[] option = {"Tomar foto", "Elegir de la galería", getString(android.R.string.cancel)};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(android.R.string.dialog_alert_title);
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // abrirCamara();//opcional
                        break;
                    case 1:
                        elegirGaleria();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void elegirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Seleccione una imagen"), STATUS_CODE_SELECTION_IMAGEN);

    }







    private void ocultarTeclado() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        // mgr.showSoftInput(etDatos, InputMethodManager.HIDE_NOT_ALWAYS);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etDescrpcion.getWindowToken(), 0);

        }
    }

    private String buscarUsuario() {

        String idUsuario = usr.get(email);

        return idUsuario;
    }


    private void crearIncidencia(String idUsuario) {
        Incidencias in;

        if (ivFoto.getDrawable()!=null) {

            in = new Incidencias(email, etDescrpcion.getText().toString(),uriFoto.toString());

        }else{
           in = new Incidencias(email, etDescrpcion.getText().toString(),"");
        }


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


        if (ivFoto.getDrawable()!=null){
            StorageReference storageRef = storage.getReference("uploads/");
            StorageReference foto = storageRef.child(System.currentTimeMillis()+".");
            foto.putFile(uriFoto);
        }



        finish();

    }

    private void leerUsurarios() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("Comunidades").document(idComunidad).collection("Usuarios_Comunidad_" + idComunidad)
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