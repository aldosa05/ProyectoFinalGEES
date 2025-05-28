package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnirseEquipoActivity extends AppCompatActivity {

    // ID del usuario actual (viene por Intent)
    private int idUsuario;

    // Campo donde se introduce el código del grupo
    private EditText etCodigoGrupo;

    // Instancia del servicio Retrofit
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Permite que la UI ocupe toda la pantalla
        setContentView(R.layout.unirse_equipo);

        // Asegura que los elementos no se solapen con barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.unirse), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencia al input
        etCodigoGrupo = findViewById(R.id.etCodigoGrupo);

        // Recuperar ID de usuario desde el intent
        idUsuario = getIntent().getIntExtra("idUsuario", -1);

        // Si por algún motivo no llega el ID, se corta la actividad
        if (idUsuario == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Inicializar cliente de API
        apiService = RetrofitClient.getApiService();
    }

    // Método que se ejecuta al pulsar el botón "Unirse"
    public void unirseAEquipo(View view) {
        String codigoGrupo = etCodigoGrupo.getText().toString().trim();

        // Validación básica del input
        if (codigoGrupo.isEmpty()) {
            Toast.makeText(this, "Introduce el código del grupo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construcción del DTO de unión
        UnirseEquipoDTO dto = new UnirseEquipoDTO(idUsuario, codigoGrupo);

        // Envío al servidor mediante Retrofit
        Call<Void> call = apiService.unirseAEquipo(dto);
        call.enqueue(new Callback<Void>() {

            // ✅ Servidor respondió (aunque podría ser error)
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Unión exitosa → Redirigir a Home
                    Toast.makeText(UnirseEquipoActivity.this, "Te has unido al equipo correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UnirseEquipoActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Limpia backstack
                    startActivity(intent);
                    finish();
                } else {
                    // Error de lógica (grupo inexistente, duplicado, etc.)
                    Toast.makeText(UnirseEquipoActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            // ❌ Error de red, timeout, servidor caído...
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UnirseEquipoActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
