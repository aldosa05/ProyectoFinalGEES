package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnyadirEquipoActivity extends AppCompatActivity {
    // Referencias a las vistas de entrada
    private EditText etNombre;
    private Spinner spinnerDeporte, spinnerCategoria;
    private CheckBox cbMultas;
    private ApiService apiService;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);// Configuración para diseño edge-to-edge (UI inmersiva)
        setContentView(R.layout.anyadir_equipo);// Establece layout principal
        // Ajuste automático de padding por insets del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.anyadir), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Inicialización de vistas
        etNombre = findViewById(R.id.etNombreEquipo);
        spinnerDeporte = findViewById(R.id.spinnerDeporte);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        cbMultas = findViewById(R.id.cbMultas);

        // ID del usuario que crea el equipo (obligatorio)
        idUsuario = getIntent().getIntExtra("idUsuario", -1); // -1 como fallback

        if (idUsuario == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            finish();
        }

        apiService = RetrofitClient.getApiService();// Inicializa Retrofit para llamadas API

    }

    public void crearEquipo(View view) {
        // Recuperación de datos de UI
        String nombre = etNombre.getText().toString().trim();
        String deporte = spinnerDeporte.getSelectedItem().toString();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        boolean tieneMultas = cbMultas.isChecked();

        // Validación básica
        if (nombre.isEmpty() || categoria.isEmpty() || deporte.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        // Construcción del DTO con los datos y códigos aleatorios
        CrearEquipoDTO dto = new CrearEquipoDTO(
                nombre,
                categoria,
                deporte,
                tieneMultas,
                generarCodigo(),
                generarCodigo(),
                generarCodigo(),
                idUsuario
        );
        // Llamada HTTP para crear el equipo
        Call<Equipo> call = apiService.crearEquipo(dto);
        call.enqueue(new Callback<Equipo>() {
            @Override
            public void onResponse(Call<Equipo> call, Response<Equipo> response) {
                if (response.isSuccessful()) {
                    // Llamada HTTP para crear el equipo
                    Toast.makeText(AnyadirEquipoActivity.this, "Equipo creado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AnyadirEquipoActivity.this, HomeActivity.class);
                    intent.putExtra("deporte", deporte);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // Fallo HTTP (por ejemplo, 400 o 500)
                    Toast.makeText(AnyadirEquipoActivity.this, "Error al crear equipo: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Equipo> call, Throwable t) {
                // Error de red u otra excepción
                Toast.makeText(AnyadirEquipoActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Genera un código aleatorio tipo GRP-12345
    private String generarCodigo() {
        return "GRP-" + (int)(Math.random() * 100000);
    }

}
