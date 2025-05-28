package com.example.proyectofinal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MultasFragment extends Fragment implements MultaAdapter.OnMultaListener {

    // UI components
    private RecyclerView recyclerMultas;
    private Button btnCrear;

    // Adapter y lista de datos
    private MultaAdapter adapter;
    private List<Multa> listaMultas = new ArrayList<>();

    // Datos recibidos por argumentos
    private int idEquipo;
    private String rol;

    public MultasFragment() {} // Constructor vacío requerido por Fragment

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar layout específico del fragment
        return inflater.inflate(R.layout.fragment_multas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle("Multas"); // Establece título en la Toolbar

        // Referencias a vistas
        recyclerMultas = view.findViewById(R.id.recyclerMultas);
        btnCrear = view.findViewById(R.id.btnCrearMulta);

        // Recuperar argumentos
        if (getArguments() != null) {
            idEquipo = getArguments().getInt("idEquipo", -1);
            rol = getArguments().getString("rol", "");
        }

        // Visibilidad del botón solo si es entrenador
        boolean esEntrenador = "entrenador".equalsIgnoreCase(rol);
        if (esEntrenador) btnCrear.setVisibility(View.VISIBLE);

        // Setup del RecyclerView
        recyclerMultas.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MultaAdapter(listaMultas, esEntrenador, this);
        recyclerMultas.setAdapter(adapter);

        // Acción botón para crear multa
        btnCrear.setOnClickListener(v -> {
            CrearMultaDialogFragment dialog = new CrearMultaDialogFragment(multa -> {
                multa.setIdEquipo(idEquipo); // asigna id del equipo actual
                Log.d("MultasFragment", "🧑 Nombre del jugador: " + multa.getNombreJugador());
                Log.d("🚀 MULTA_JSON", new Gson().toJson(multa));
                crearMulta(multa); // llamada a backend
            });
            dialog.show(getChildFragmentManager(), "crear_multa");
        });

        // Carga inicial de datos
        cargarMultas();
    }

    /**
     * Realiza una petición GET para obtener las multas del equipo
     */
    private void cargarMultas() {
        ApiService api = RetrofitClient.getApiService();
        api.obtenerMultas(idEquipo).enqueue(new Callback<List<Multa>>() {
            @Override
            public void onResponse(Call<List<Multa>> call, Response<List<Multa>> response) {
                if (response.isSuccessful()) {
                    listaMultas.clear();
                    listaMultas.addAll(response.body());
                    adapter.notifyDataSetChanged(); // refresca lista
                }
            }

            @Override
            public void onFailure(Call<List<Multa>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Crea una nueva multa en el servidor
     */
    private void crearMulta(Multa multa) {
        ApiService api = RetrofitClient.getApiService();
        api.crearMulta(multa).enqueue(new Callback<Multa>() {
            @Override
            public void onResponse(Call<Multa> call, Response<Multa> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Multa añadida", Toast.LENGTH_SHORT).show();
                    cargarMultas(); // Refresca la vista tras añadir
                }
            }

            @Override
            public void onFailure(Call<Multa> call, Throwable t) {
                Toast.makeText(getContext(), "Error al crear multa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Llamado cuando el usuario quiere eliminar una multa desde el adapter
     */
    @Override
    public void onEliminarMulta(int idMulta) {
        ApiService api = RetrofitClient.getApiService();
        api.eliminarMulta(idMulta).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cargarMultas(); // Refresca tras borrado
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Llamado cuando el usuario marca una multa como pagada desde el adapter
     */
    @Override
    public void onMarcarPagada(int idMulta) {
        ApiService api = RetrofitClient.getApiService();
        api.marcarMultaPagada(idMulta).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cargarMultas(); // Actualiza estado visual
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error al marcar como pagada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


