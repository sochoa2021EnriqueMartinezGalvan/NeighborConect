package net.iessochoa.neighborconect.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.iessochoa.neighborconect.CrearComunidad;
import net.iessochoa.neighborconect.IntroducirCodigo;
import net.iessochoa.neighborconect.MainActivity;
import net.iessochoa.neighborconect.R;
import net.iessochoa.neighborconect.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;



    private TextView tvUsuario, tvCerrarSesion;
    private Button btCrear,btEntrar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        btCrear=binding.btCrear;
        btEntrar=binding.btEntar;


        btCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearComunidad();
            }
        });
        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrarComunidad();
            }
        });





        return root;



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void crearComunidad() {

        startActivity(new Intent(getActivity(), CrearComunidad.class));

    }

    private void entrarComunidad() {
        startActivity(new Intent(getActivity(), IntroducirCodigo.class));
    }
}