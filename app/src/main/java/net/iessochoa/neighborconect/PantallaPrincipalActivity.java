package net.iessochoa.neighborconect;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import net.iessochoa.neighborconect.databinding.ActivityPantallaPrincipalBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PantallaPrincipalActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPantallaPrincipalBinding binding;

    private FirebaseAuth mAuth;

    private TextView tvUsuario, tvCerrarSesion;

    private int numComunidadesUsuario=0;

    private String email;

    private ArrayList<String> idComunidadesList = new ArrayList();
    private ArrayList<String> idComunidadesList2 = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPantallaPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarPantallaPrincipal.toolbar);
        /*binding.appBarPantallaPrincipal.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_pantalla_principal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        NavigationView navigation = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigation.getHeaderView(0);

        tvUsuario = headerView.findViewById(R.id.tvUsuario);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFB = mAuth.getCurrentUser();

        tvUsuario.setText(userFB.getEmail());

        email = userFB.getEmail();


        tvCerrarSesion = headerView.findViewById(R.id.tvCerrarSesion);
        tvCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        idComunidades();
        idComunidades2();


    }

    private void idComunidades2() {


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pantalla_principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_pantalla_principal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void cerrarSesion() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));

    }

    public void crearComunidad() {

        startActivity(new Intent(this, CrearComunidad.class));

    }

    private void entrarComunidad() {
        startActivity(new Intent(this, IntroducirCodigo.class));
    }

    private void numComunidadesusuario() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (String str : idComunidadesList) {


            db.collection("Comunidades").document(str).collection("Usuarios_Comunidad").whereEqualTo("email",email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " +
                                            document.getData());
                                    numComunidadesUsuario+=1;
                                    idComunidadesList2.add(str);
                                }
                                SharedPreferences settings = getSharedPreferences("ncu", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();

                                Set<String> set = new HashSet<String>();

                                set.addAll(idComunidadesList2);

                                editor.putInt("ncu",numComunidadesUsuario);
                                editor.putStringSet("idCom",set);
                                editor.commit();


                            } else {
                                Log.d(TAG, "Error getting documents: ",
                                        task.getException());
                            }
                        }
                    });



        }




    }

    private void idComunidades() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Comunidades").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " +
                                        document.getData());

                                idComunidadesList.add(document.getId());
                            }
                            numComunidadesusuario();



                        } else {
                            Log.d(TAG, "Error getting documents: ",
                                    task.getException());
                        }
                    }
                });

    }

}