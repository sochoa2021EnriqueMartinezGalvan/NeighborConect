package net.iessochoa.neighborconect.ui.Inicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.iessochoa.neighborconect.CrearComunidad;
import net.iessochoa.neighborconect.IntroducirCodigo;
import net.iessochoa.neighborconect.databinding.FragmentHomeBinding;

public class InicioFragment extends Fragment {

    private InicioViewModel inicioViewModel;
    private FragmentHomeBinding binding;



    private TextView tvUsuario, tvCerrarSesion;
    private Button btCrear,btEntrar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel =
                new ViewModelProvider(this).get(InicioViewModel.class);

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