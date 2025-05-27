package com.example.proyectofinal;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MultasFragment extends Fragment implements MultaAdapter.OnMultaListener {

    private RecyclerView recyclerMultas;
    private Button btnCrear;
    private MultaAdapter adapter;
    private List<Multa> listaMultas = new ArrayList<>();

    private int idEquipo;
    private String rol;

    public MultasFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().setTitle("Multas");

        recyclerMultas = view.findViewById(R.id.recyclerMultas);
        btnCrear = view.findViewById(R.id.btnCrearMulta);

        if (getArguments() != null) {
            idEquipo = getArguments().getInt("idEquipo", -1);
            rol = getArguments().getString("rol", "");
        }

        boolean esEntrenador = "entrenador".equalsIgnoreCase(rol);
        if (esEntrenador) btnCrear.setVisibility(View.VISIBLE);

        recyclerMultas.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MultaAdapter(listaMultas, esEntrenador, this);
        recyclerMultas.setAdapter(adapter);

        btnCrear.setOnClickListener(v -> {
            CrearMultaDialogFragment dialog = new CrearMultaDialogFragment(multa -> {
                multa.setIdEquipo(idEquipo);
                crearMulta(multa);
            });
            dialog.show(getChildFragmentManager(), "crear_multa");
        });

        cargarMultas();
    }

    private void cargarMultas() {
        ApiService api = RetrofitClient.getApiService();
        api.obtenerMultas(idEquipo).enqueue(new Callback<List<Multa>>() {
            @Override
            public void onResponse(Call<List<Multa>> call, Response<List<Multa>> response) {
                if (response.isSuccessful()) {
                    listaMultas.clear();
                    listaMultas.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Multa>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearMulta(Multa multa) {
        ApiService api = RetrofitClient.getApiService();
        api.crearMulta(multa).enqueue(new Callback<Multa>() {
            @Override
            public void onResponse(Call<Multa> call, Response<Multa> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Multa añadida", Toast.LENGTH_SHORT).show();
                    cargarMultas();
                }
            }

            @Override
            public void onFailure(Call<Multa> call, Throwable t) {
                Toast.makeText(getContext(), "Error al crear multa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEliminarMulta(int idMulta) {
        ApiService api = RetrofitClient.getApiService();
        api.eliminarMulta(idMulta).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cargarMultas();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMarcarPagada(int idMulta) {
        ApiService api = RetrofitClient.getApiService();
        api.marcarMultaPagada(idMulta).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cargarMultas();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error al marcar como pagada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

