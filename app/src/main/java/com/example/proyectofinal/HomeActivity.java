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
    private LinearLayout llEquipos;
    private int idUsuario;
    UsuarioEquipoDTO dto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1) Referencia al contenedor donde meteremos los equipos
        llEquipos = findViewById(R.id.layoutEquipos);

        // Recuperamos todos los extras enviados por MainActivity
        idUsuario = getIntent().getIntExtra("id_usuario", -1);
        String nombreUsuario = getIntent().getStringExtra("nombre_usuario");
        String correoUsuario = getIntent().getStringExtra("correo_usuario");

        Log.d("HomeActivity", "🔑 idUsuario: " + idUsuario);
        Log.d("HomeActivity", "👤 nombreUsuario: " + nombreUsuario);
        Log.d("HomeActivity", "📧 correoUsuario: " + correoUsuario);

        if (idUsuario == -1) {
            // No llegó el id: manejar el error (quizá volver al login)
            Toast.makeText(this, "Error: usuario no identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarEquiposDelUsuario();
    }
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

    /** Construye la UI dinámicamente para cada equipo */
    private void mostrarEquipos(List<UsuarioEquipoDTO> ueList) {
        llEquipos.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (UsuarioEquipoDTO dto : ueList) {
            View item = inflater.inflate(R.layout.item_equipo, llEquipos, false);

            TextView tvNombre = item.findViewById(R.id.tvNombreEquipo);
            TextView tvRol = item.findViewById(R.id.tvRolEquipo);

            tvNombre.setText(dto.getNombreEquipo() != null ? dto.getNombreEquipo() : "Equipo sin nombre");
            tvRol.setText(dto.getRol() != null ? dto.getRol() : "Sin rol");

            item.setOnClickListener(v -> entrarAlEquipo(dto));
            llEquipos.addView(item);
        }
    }



    private void entrarAlEquipo(UsuarioEquipoDTO equipo) {
        if (equipo == null) {
            Toast.makeText(this, "Error: equipo no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        int idEquipo = equipo.getIdEquipo();
        String rol = equipo.getRol(); // o donde tengas guardado el rol

        Intent intent = new Intent(this, MainEquipoActivity.class);
        intent.putExtra("idEquipo", idEquipo);
        Log.d("HomeActivity", "🔑 idEquipo: " + idEquipo);
        intent.putExtra("rol", rol);
        startActivity(intent);
    }



    public void AnyadirEquipo(View view) {

        Intent intent = new Intent(HomeActivity.this, AnyadirEquipoActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);

    }

    public void UnirseEquipo(View view) {
        Intent intent = new Intent(HomeActivity.this, UnirseEquipoActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
    }
}
