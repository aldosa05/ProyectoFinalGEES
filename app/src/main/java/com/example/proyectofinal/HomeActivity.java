package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout llEquipos; // 🧱 Layout dinámico donde se inflan las vistas de los equipos
    private int idUsuario; // 🔐 ID del usuario actual
    UsuarioEquipoDTO dto; // 🧾 DTO reutilizable para transporte de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // 🧩 Habilita modo Edge-to-Edge para diseño moderno
        setContentView(R.layout.activity_home);

        // 🧱 Aplica padding automático por insets del sistema (barra de estado, navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔗 Vincula el LinearLayout que contendrá la lista de equipos
        llEquipos = findViewById(R.id.layoutEquipos);

        // 📦 Recuperamos datos enviados desde MainActivity (login)
        idUsuario = getIntent().getIntExtra("id_usuario", -1);
        String nombreUsuario = getIntent().getStringExtra("nombre_usuario");
        String correoUsuario = getIntent().getStringExtra("correo_usuario");

        Log.d("HomeActivity", "🔑 idUsuario: " + idUsuario);
        Log.d("HomeActivity", "👤 nombreUsuario: " + nombreUsuario);
        Log.d("HomeActivity", "📧 correoUsuario: " + correoUsuario);

        // ❌ Validación de existencia de ID
        if (idUsuario == -1) {
            Toast.makeText(this, "Error: usuario no identificado", Toast.LENGTH_SHORT).show();
            finish(); // Salir si no hay usuario válido
            return;
        }

        // 🔄 Llamada para obtener los equipos a los que pertenece el usuario
        cargarEquiposDelUsuario();
    }

    /**
     * 🔁 Hace una llamada al servidor para obtener los equipos del usuario actual
     */
    private void cargarEquiposDelUsuario() {
        ApiService api = RetrofitClient.getApiService();

        api.getEquiposInfo(idUsuario).enqueue(new Callback<List<UsuarioEquipoDTO>>() {
            @Override
            public void onResponse(Call<List<UsuarioEquipoDTO>> call, Response<List<UsuarioEquipoDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mostrarEquipos(response.body());
                } else {
                    Toast.makeText(HomeActivity.this, "Error al cargar equipos", Toast.LENGTH_SHORT).show();
                    Log.w("HomeActivity", "Código HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioEquipoDTO>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeActivity", "❌ Error de red", t);
            }
        });
    }

    /**
     * 🧱 Muestra dinámicamente los equipos del usuario en el layout usando item_equipo.xml
     */
    private void mostrarEquipos(List<UsuarioEquipoDTO> ueList) {
        llEquipos.removeAllViews(); // 🔄 Limpia el layout por si ya había contenido
        LayoutInflater inflater = LayoutInflater.from(this);

        for (UsuarioEquipoDTO dto : ueList) {
            // 📦 Cargamos el layout de cada item de equipo
            View item = inflater.inflate(R.layout.item_equipo, llEquipos, false);

            TextView tvNombre = item.findViewById(R.id.tvNombreEquipo);
            TextView tvRol = item.findViewById(R.id.tvRolEquipo);

            // 🏷 Asignamos los valores
            tvNombre.setText(dto.getNombreEquipo() != null ? dto.getNombreEquipo() : "Equipo sin nombre");
            tvRol.setText(dto.getRol() != null ? dto.getRol() : "Sin rol");

            // 🎯 Acción al pulsar: entrar a ese equipo
            item.setOnClickListener(v -> entrarAlEquipo(dto));

            llEquipos.addView(item); // ➕ Agregamos al layout
        }
    }

    /**
     * 🚪 Abre la actividad principal de un equipo (MainEquipoActivity) con sus datos
     */
    private void entrarAlEquipo(UsuarioEquipoDTO equipo) {
        if (equipo == null) {
            Toast.makeText(this, "Error: equipo no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // 📦 Extraemos datos del equipo
        int idEquipo = equipo.getIdEquipo();
        String rol = equipo.getRol();
        String deporte = equipo.getDeporte();
        String NombreEquipo = equipo.getNombreEquipo();
        boolean UsaMultas = equipo.isUsaMultas();

        // 🔁 Creamos el intent para ir a la siguiente pantalla
        Intent intent = new Intent(this, MainEquipoActivity.class);
        intent.putExtra("idEquipo", idEquipo);
        intent.putExtra("rol", rol);
        intent.putExtra("deporte", deporte);
        intent.putExtra("NombreEquipo", NombreEquipo);
        intent.putExtra("usaMultas", UsaMultas);
        intent.putExtra("idUsuario", idUsuario);

        // 🧪 Logs para debugging
        Log.d("HomeActivity", "🔑 Enviando idUsuario: " + idUsuario);
        Log.d("HomeActivity", "🔑 NombreEquipo: " + NombreEquipo);
        Log.d("HomeActivity", "🔑 Deporte: " + deporte);

        startActivity(intent); // 🚀 Lanzamos actividad
    }

    /**
     * ➕ Crea un equipo nuevo (va a AnyadirEquipoActivity)
     */
    public void AnyadirEquipo(View view) {
        Intent intent = new Intent(HomeActivity.this, AnyadirEquipoActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
    }

    /**
     * ➕ Unirse a un equipo existente
     */
    public void UnirseEquipo(View view) {
        Intent intent = new Intent(HomeActivity.this, UnirseEquipoActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
    }
}

