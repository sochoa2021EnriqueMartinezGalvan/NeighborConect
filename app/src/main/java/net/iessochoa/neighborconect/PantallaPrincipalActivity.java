package net.iessochoa.neighborconect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import net.iessochoa.neighborconect.databinding.ActivityPantallaPrincipalBinding;

public class PantallaPrincipalActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPantallaPrincipalBinding binding;

    private FirebaseAuth mAuth;

    private TextView tvUsuario, tvCerrarSesion;
    private Button btCrear;


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

        btCrear = findViewById(R.id.btCrear);

        btCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearComunidad();
            }
        });


        NavigationView navigation = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigation.getHeaderView(0);

        tvUsuario = headerView.findViewById(R.id.tvUsuario);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFB = mAuth.getCurrentUser();

        tvUsuario.setText(userFB.getEmail());

        tvCerrarSesion = headerView.findViewById(R.id.tvCerrarSesion);
        tvCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });


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

}