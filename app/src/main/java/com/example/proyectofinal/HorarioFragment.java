package com.example.proyectofinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HorarioFragment extends Fragment {

    private LinearLayout listaHorarios;
    private Button btnModificar;
    private int idEquipo;
    private String rol;

    public HorarioFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_horario, container, false); // Usamos mismo layout
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // INSETS para status bar
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.horario), (v, insets) -> {
            int type = WindowInsetsCompat.Type.systemBars();
            v.setPadding(
                    insets.getInsets(type).left,
                    insets.getInsets(type).top,
                    insets.getInsets(type).right,
                    insets.getInsets(type).bottom
            );
            return insets;
        });

        // 🔄 Obtener datos pasados por arguments
        if (getArguments() != null) {
            idEquipo = getArguments().getInt("idEquipo", -1);
            Log.d("HorarioFragment", "🔑 idEquipo: " + idEquipo);
            rol = getArguments().getString("rol", null);
        }

        if (idEquipo == -1 || rol == null) {
            Toast.makeText(getContext(), "Datos insuficientes", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            return;
        }

        Log.d("HorarioFragment", "🔑 idEquipo: " + idEquipo + " | rol: " + rol);

        // Inicializar vistas
        btnModificar = view.findViewById(R.id.btnModificarHorario);
        listaHorarios = view.findViewById(R.id.listaHorarios);

        // Mostrar u ocultar botón según rol
        if (!"entrenador".equalsIgnoreCase(rol)) {
            btnModificar.setVisibility(View.GONE);
        }

        btnModificar.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ModificarHorarioActivity.class);
            intent.putExtra("idEquipo", idEquipo);
            intent.putExtra("rol", rol);
            startActivity(intent);
        });

        cargarHorarios();
    }

    private void cargarHorarios() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Horario>> call = apiService.getHorariosPorEquipo(idEquipo);

        call.enqueue(new Callback<List<Horario>>() {
            @Override
            public void onResponse(Call<List<Horario>> call, Response<List<Horario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mostrarHorarios(response.body());
                } else {
                    Toast.makeText(getContext(), "Error cargando horarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Horario>> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarHorarios(List<Horario> horarios) {
        listaHorarios.removeAllViews();

        for (Horario h : horarios) {
            TextView tv = new TextView(getContext());
            tv.setText(h.getDia() + " - Quedada: " + h.getHora_quedada() + ", Entreno: " + h.getHora_entreno() + ", Lugar: " + h.getLugar());
            tv.setPadding(8, 16, 8, 16);
            tv.setTextSize(16);
            listaHorarios.addView(tv);
        }
    }
}
