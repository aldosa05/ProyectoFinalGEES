package com.example.proyectofinal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

    // 🧱 Referencias UI
    private LinearLayout listaHorarios;
    private Button btnModificar;

    // 📦 Variables recibidas por argumentos
    private int idEquipo;
    private String rol;

    public HorarioFragment() {
        // Constructor obligatorio vacío
    }

    // 🧩 Inflamos el layout principal desde un XML compartido con la actividad
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_horario, container, false);
    }

    // 🎯 Lógica de inicialización y eventos
    @SuppressLint("MissingInflatedId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle("Horarios");

        // 🧱 Acomoda padding según las barras del sistema (Edge-to-Edge)
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

        // 📦 Cargamos datos enviados desde el fragmento anterior
        if (getArguments() != null) {
            idEquipo = getArguments().getInt("idEquipo", -1);
            rol = getArguments().getString("rol", null);
        }

        // ❌ Validación temprana
        if (idEquipo == -1 || rol == null) {
            Toast.makeText(getContext(), "Datos insuficientes", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            return;
        }

        Log.d("HorarioFragment", "🔑 idEquipo: " + idEquipo + " | rol: " + rol);

        // 🎯 Vinculamos elementos de la vista
        btnModificar = view.findViewById(R.id.btnModificarHorario);
        listaHorarios = view.findViewById(R.id.listaHorarios);

        // 🔐 Mostrar el botón solo si es entrenador
        if (!"entrenador".equalsIgnoreCase(rol)) {
            btnModificar.setVisibility(View.GONE);
        }

        // 📝 Abrir actividad de modificación
        btnModificar.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ModificarHorarioActivity.class);
            intent.putExtra("idEquipo", idEquipo);
            intent.putExtra("rol", rol);
            startActivity(intent);
        });

        // 🔄 Cargar horarios al iniciar
        cargarHorarios();
    }

    /**
     * 🔁 Llama a la API para obtener horarios del equipo
     */
    private void cargarHorarios() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Horario>> call = apiService.getHorariosPorEquipo(idEquipo);

        call.enqueue(new Callback<List<Horario>>() {
            @Override
            public void onResponse(Call<List<Horario>> call, Response<List<Horario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mostrarHorarios(response.body()); // ✅ Renderizar lista
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

    /**
     * 🧱 Renderiza dinámicamente la lista de horarios
     */
    private void mostrarHorarios(List<Horario> horarios) {
        listaHorarios.removeAllViews(); // 🧼 Limpiar lista anterior

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (Horario h : horarios) {
            View itemView = inflater.inflate(R.layout.item_horario, listaHorarios, false);

            TextView tvFecha = itemView.findViewById(R.id.tvFecha);
            TextView tvLugar = itemView.findViewById(R.id.tvLugar);
            TextView tvQuedada = itemView.findViewById(R.id.tvQuedada);
            TextView tvEntreno = itemView.findViewById(R.id.tvEntreno);
            Button btnEliminar = itemView.findViewById(R.id.btnEliminarHorario);

            // 📅 Mostrar datos
            tvFecha.setText("🗓️ " + h.getDia());
            tvLugar.setText("📍 Lugar: " + h.getLugar());
            tvQuedada.setText("⏰ Quedada: " + h.getHora_quedada());
            tvEntreno.setText("🏋️ Entreno: " + h.getHora_entreno());

            // 🔐 Si es entrenador, se permite eliminar
            if ("entrenador".equalsIgnoreCase(rol)) {
                btnEliminar.setVisibility(View.VISIBLE);
                btnEliminar.setOnClickListener(v -> confirmarEliminarHorario(h.getId()));
            } else {
                btnEliminar.setVisibility(View.GONE);
            }

            listaHorarios.addView(itemView); // ➕ Agregamos al layout
        }
    }

    /**
     * 🔒 Muestra diálogo de confirmación antes de eliminar un horario
     */
    private void confirmarEliminarHorario(int idHorario) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar horario")
                .setMessage("¿Estás seguro de que deseas eliminar este horario?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarHorario(idHorario))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * ❌ Llama al endpoint para borrar un horario concreto
     */
    private void eliminarHorario(int idHorario) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.eliminarHorario(idHorario).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Horario eliminado", Toast.LENGTH_SHORT).show();
                    cargarHorarios(); // 🔁 Refrescar la lista
                } else {
                    Toast.makeText(getContext(), "Error al eliminar horario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

