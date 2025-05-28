package com.example.proyectofinal;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvocatoriaFragment extends Fragment {
    // Elementos de la UI relacionados con la convocatoria
    private TextView tvFecha, tvLugar, tvHoraPartido, tvHoraQuedada;
    private TextView tvEquipoLocal, tvEquipoRival;
    private LinearLayout gridJugadores;
    private Button btnCrearConvocatoria, btnAnadirJugadores, btnEliminarConvocatoria;

    // Datos de contexto del fragmento (rol del usuario, nombre del equipo, etc.)
    private int idEquipo;
    private String rol;
    private String NombreEquipo;

    // Objeto que representa la convocatoria actual cargada desde el backend
    private Convocatoria convocatoriaActual;

    public ConvocatoriaFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_convocatoria, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        requireActivity().setTitle("Convocatoria");

        // Recupera los argumentos que se pasan al fragmento
        if (getArguments() != null) {
            idEquipo = getArguments().getInt("idEquipo", -1);
            rol = getArguments().getString("rol", "");
            NombreEquipo = getArguments().getString("NombreEquipo", "");
        }

        // Inicializa referencias a elementos visuales del layout
        tvFecha = view.findViewById(R.id.tvFecha);
        tvLugar = view.findViewById(R.id.tvLugar);
        tvHoraPartido = view.findViewById(R.id.tvHoraPartido);
        tvHoraQuedada = view.findViewById(R.id.tvHoraQuedada);
        tvEquipoLocal = view.findViewById(R.id.tvEquipoLocal);
        tvEquipoRival = view.findViewById(R.id.tvEquipoRival);
        gridJugadores = view.findViewById(R.id.grid_jugadores);
        btnCrearConvocatoria = view.findViewById(R.id.btnCrearConvocatoria);
        btnAnadirJugadores = view.findViewById(R.id.btnAnadirJugadores);
        btnEliminarConvocatoria = view.findViewById(R.id.btnEliminarConvocatoria);

        // Oculta botones de edición si no es entrenador
        if (!"entrenador".equalsIgnoreCase(rol)) {
            btnCrearConvocatoria.setVisibility(View.GONE);
            btnAnadirJugadores.setVisibility(View.GONE);
            btnEliminarConvocatoria.setVisibility(View.GONE);
        }else{
            btnCrearConvocatoria.setVisibility(View.VISIBLE);
            btnAnadirJugadores.setVisibility(View.VISIBLE);
            btnEliminarConvocatoria.setVisibility(View.VISIBLE);
        }

        // Ir a crear convocatoria
        btnCrearConvocatoria.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CrearConvocatoriaActivity.class);
            intent.putExtra("idEquipo", idEquipo);
            intent.putExtra("NombreEquipo", NombreEquipo);
            startActivity(intent);
        });
        // Ir a añadir jugadores a la convocatoria
            if (btnAnadirJugadores != null) {
                btnAnadirJugadores.setOnClickListener(v -> {
                    if (convocatoriaActual != null) {
                        Intent intent = new Intent(requireContext(), AnadirConvocadoActivity.class);
                        intent.putExtra("idPartido", convocatoriaActual.getIdPartido());
                        startActivity(intent);
                    }  // Tu lógica
                });
            } else {
                Log.e("ConvocatoriaFragment", "❌ btnAnadirJugadores no encontrado en el layout.");
            }
        // Confirmación para eliminar convocatoria
        btnEliminarConvocatoria.setOnClickListener(v -> {
            if (convocatoriaActual != null) {
                confirmarEliminacion(convocatoriaActual.getIdPartido());
            }
        });

        // Carga inicial de datos
        cargarConvocatoria();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarConvocatoria(); // Refresca la vista al volver
    }

    // Llama al backend para obtener la última convocatoria del equipo
    private void cargarConvocatoria() {
        ApiService api = RetrofitClient.getApiService();
        api.getConvocatoriaPorEquipo(idEquipo).enqueue(new Callback<Convocatoria>() {
            @Override
            public void onResponse(Call<Convocatoria> call, Response<Convocatoria> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mostrarConvocatoria(response.body());
                } else {
                    Toast.makeText(requireContext(), "No hay convocatoria aún", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Convocatoria> call, Throwable t) {
                Toast.makeText(requireContext(), "Fallo al cargar convocatoria", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Pinta los datos de la convocatoria actual en pantalla
    private void mostrarConvocatoria(Convocatoria c) {
        convocatoriaActual = c;

        tvFecha.setText("Día: " + c.getFecha());
        tvLugar.setText("Lugar: " + c.getLugar());
        tvHoraPartido.setText("Hora del partido: " + c.getHoraPartido());
        tvHoraQuedada.setText("Hora de quedada: " + c.getHoraQuedada());
        tvEquipoLocal.setText(NombreEquipo);
        tvEquipoRival.setText(c.getEquipoRival());

        cargarConvocados(c.getIdPartido());// Trae jugadores
    }

    // Solicita al backend los jugadores convocados para la convocatoria actual
    private void cargarConvocados(int idPartido) {
        ApiService api = RetrofitClient.getApiService();
        api.getConvocadosPorPartido(idPartido).enqueue(new Callback<List<Convocado>>() {
            @Override
            public void onResponse(Call<List<Convocado>> call, Response<List<Convocado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Convocado> convocados = response.body();
                    System.out.println("📲 Convocados recibidos del backend: " + convocados.size());
                    for (Convocado c : convocados) {
                        System.out.println("🧍‍♂️ Nombre: " + c.getNombre() + " | Dorsal: " + c.getDorsal());
                    }
                    mostrarConvocados(convocados);
                } else {
                    System.out.println("⚠️ Respuesta vacía o mal formada.");
                    mostrarConvocados(null);
                }
            }

            @Override
            public void onFailure(Call<List<Convocado>> call, Throwable t) {
                Toast.makeText(requireContext(), "❌ Error al cargar convocados", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Imprime los nombres de los convocados dinámicamente en el layout
    private void mostrarConvocados(List<Convocado> convocados) {
        gridJugadores.removeAllViews();

        if (convocados != null && !convocados.isEmpty()) {
            for (Convocado c : convocados) {
                TextView tvJugador = new TextView(requireContext());
                tvJugador.setText("• " + c.getNombre() + " (Dorsal: " + c.getDorsal() + ")");
                tvJugador.setTextSize(16);
                tvJugador.setTextColor(getResources().getColor(android.R.color.black));
                tvJugador.setPadding(8, 8, 8, 8);
                tvJugador.setTypeface(null, Typeface.BOLD);
                tvJugador.setGravity(Gravity.START);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(8, 8, 8, 8);
                tvJugador.setLayoutParams(params);


                gridJugadores.addView(tvJugador);
            }
        } else {
            TextView noHay = new TextView(requireContext());
            noHay.setText("Sin jugadores convocados");
            noHay.setTextColor(getResources().getColor(android.R.color.darker_gray));
            gridJugadores.addView(noHay);
        }
    }


    // Diálogo de confirmación para eliminar convocatoria
    private void confirmarEliminacion(int idPartido) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar convocatoria")
                .setMessage("¿Estás seguro de que quieres eliminar esta convocatoria?")
                .setPositiveButton("Sí", (dialog, which) -> eliminarConvocatoria(idPartido))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Llama al backend para borrar la convocatoria y limpia la UI
    private void eliminarConvocatoria(int idPartido) {
        ApiService api = RetrofitClient.getApiService();
        api.eliminarConvocatoria(idPartido).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(requireContext(), "Convocatoria eliminada ✅", Toast.LENGTH_SHORT).show();
                convocatoriaActual = null;
                gridJugadores.removeAllViews();// limpia la lista de nombres
                tvFecha.setText("");
                tvLugar.setText("");
                tvHoraPartido.setText("");
                tvHoraQuedada.setText("");
                tvEquipoRival.setText("");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "❌ Error al eliminar convocatoria", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
