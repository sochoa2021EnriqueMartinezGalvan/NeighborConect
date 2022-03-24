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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicioSesionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText etEmailInicio, etPassInicio;
    private Button btInicarSesion;
    private TextView tvPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        etEmailInicio = findViewById(R.id.etEmailInicio);

        etPassInicio = findViewById(R.id.etPassInicio);
        btInicarSesion = findViewById(R.id.btIniciarSesion);


        mAuth = FirebaseAuth.getInstance();

        btInicarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });



    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-nul1) and update UI accordingly.
        FirebaseUser currentuser = mAuth.getCurrentUser();
        //updateUI(currentuser);
    }

    public void iniciarSesion() {
        mAuth.signInWithEmailAndPassword(etEmailInicio.getText().toString().trim(), etPassInicio.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "singInWithEmailEmail:success");
                            Toast.makeText(getApplicationContext(), getString(R.string.iniciando_sesion), Toast.LENGTH_SHORT).show();
                            FirebaseUser userFB = mAuth.getCurrentUser();
                            //updatelI(user);
                           Intent i = new Intent(getApplicationContext(),PantallaPrincipalActivity.class);
                           startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "singInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), getString(R.string.error_inicio_sesion), Toast.LENGTH_SHORT).show();
                            //updatelI(null);
                        }
                        //...
                    }
                });
    }



}