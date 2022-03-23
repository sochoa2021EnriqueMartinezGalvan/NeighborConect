package net.iessochoa.neighborconect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void registrarse(View view){
        Intent i = new Intent(this,RegistrarseActivity.class);
        startActivity(i);
    }
    public void iniciarSesion(View view){
        Intent i = new Intent(this,InicioSesionActivity.class);
        startActivity(i);
    }


}