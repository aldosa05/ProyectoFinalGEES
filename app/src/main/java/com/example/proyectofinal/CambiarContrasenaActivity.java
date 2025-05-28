package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarContrasenaActivity  extends AppCompatActivity {
    // Inputs para el correo, nueva contraseña y confirmación
    private EditText etCorreo, etNuevaContrasena, etConfirmarContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cambiar_contrasena);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cambiarcontrasena), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Referencias a los campos del formulario
        etCorreo = findViewById(R.id.etCorreo);
        etNuevaContrasena = findViewById(R.id.etNuevaContrasena);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
    }

    // Lógica al pulsar el botón de cambiar contraseña
    public void cambiarContrasena(View view) {
        // Extrae los valores introducidos
        String correo = etCorreo.getText().toString().trim();
        String nuevaContrasena = etNuevaContrasena.getText().toString().trim();
        String confirmarContrasena = etConfirmarContrasena.getText().toString().trim();
        // Validación: todos los campos obligatorios
        if (correo.isEmpty() || nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validación: las contraseñas deben coincidir
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }
        // Crea DTO para enviar al backend
        CambioContrasenaRequest request = new CambioContrasenaRequest(correo, nuevaContrasena);
        // Prepara Retrofit
        ApiService apiService = RetrofitClient.getApiService();

        Call<ResponseBody> call = apiService.cambiarContrasena(request);
        // Lanza petición
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // ✅ Contraseña cambiada correctamente
                    Toast.makeText(CambiarContrasenaActivity.this, "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                    // Redirige a la pantalla de login (MainActivity)
                    Intent intent = new Intent(CambiarContrasenaActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // ⚠️ Algo salió mal en el servidor
                    Toast.makeText(CambiarContrasenaActivity.this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CambiarContrasenaActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
