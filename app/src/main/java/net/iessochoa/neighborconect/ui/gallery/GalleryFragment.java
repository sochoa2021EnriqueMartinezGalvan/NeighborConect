package net.iessochoa.neighborconect.ui.gallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import net.iessochoa.neighborconect.InicioSesionActivity;
import net.iessochoa.neighborconect.R;
import net.iessochoa.neighborconect.adapter.ComunidadAdapter;
import net.iessochoa.neighborconect.adapter.IncidenciasAdapter;
import net.iessochoa.neighborconect.databinding.FragmentGalleryBinding;
import net.iessochoa.neighborconect.model.Comunidades;
import net.iessochoa.neighborconect.model.Incidencias;

import java.util.ArrayList;
import java.util.Set;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser userFB;

    private TextView tvUsuarioPerfil, tvNCU;
    private Button btCambiarPass;
    private String email;
    private int numComunidadesUsuario = 0;

    RecyclerView rvComunidades;
    ComunidadAdapter adapter;


    private ArrayList<String> idComunidades = new ArrayList();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        SharedPreferences sharedPref = getActivity().getSharedPreferences("ncu", Activity.MODE_PRIVATE);

        int ncu = sharedPref.getInt("ncu", -1);
        Set<String> set = sharedPref.getStringSet("idCom", null);

        for (String s : set) {
            System.out.println(s + "--------");
        }


        mAuth = FirebaseAuth.getInstance();
        userFB = mAuth.getCurrentUser();

        email = userFB.getEmail();


        rvComunidades = binding.rvComunidades;
        rvComunidades.setLayoutManager(new LinearLayoutManager(getContext()));

        defineAdaptador();

        tvUsuarioPerfil = binding.tvUsuarioPerfil;
        btCambiarPass = binding.btCambiarPass;
        tvNCU = binding.tvNCU;

        tvUsuarioPerfil.setText("Usuario: " + userFB.getEmail());
        btCambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarAlertDialog();
                Toast.makeText(getContext(), "Contraseña actualizada", Toast.LENGTH_SHORT);
            }
        });

        tvNCU.setText("Numero de comunidades en las que estas: " + String.valueOf(ncu));

        return root;
    }

    private void llamarAlertDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_pass_change, null);

        EditText etPass1 = view.findViewById(R.id.etPass1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view).setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                userFB.updatePassword(etPass1.getText().toString());
                startActivity(new Intent(getContext(), InicioSesionActivity.class));

            }
        })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.create().show();
    }

    private void defineAdaptador() {

        //consulta en Firebase
        Query query = FirebaseFirestore.getInstance()
//coleccion conferencias
                .collection("Usuarios")
//documento: conferencia actual
                .document(email)
//colección chat de la conferencia
                .collection("Comunidades");//Creamos la opciones del FirebaseAdapter
        FirestoreRecyclerOptions<Comunidades> options = new
                FirestoreRecyclerOptions.Builder<Comunidades>()
//consulta y clase en la que se guarda los datos
                .setQuery(query, Comunidades.class)
                .setLifecycleOwner(this)
                .build();
//si el usuario ya habia seleccionado otra conferencia, paramos las        escucha
        if (adapter != null) {
            adapter.stopListening();
        }
//Creamos el adaptador
adapter = new ComunidadAdapter(options);
//asignamos el adaptador
        rvComunidades.setAdapter(adapter);
//comenzamos a escuchar. Normalmente solo tenemos un adaptador, esto  tenemos que
        //hacerlo en el evento onStar, como indica la documentación
        adapter.startListening();
//Podemos reaccionar ante cambios en la query( se añade un mesaje).Nosotros,
        // //lo que necesitamos es mover el scroll
        // del recyclerView al inicio para ver el mensaje nuevo
        adapter.getSnapshots().addChangeEventListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull
                    DocumentSnapshot snapshot, int newIndex, int oldIndex) {
                rvComunidades.smoothScrollToPosition(0);
            }

            @Override
            public void onDataChanged() {
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
            }
        });


    }
    //es necesario parar la escucha
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}