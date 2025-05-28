package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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


public class RegistroActivity extends AppCompatActivity {

    // Inputs de formulario
    private EditText etUsuario, etCorreo, etPassword, etConfirmarPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        // Ajuste de padding para evitar solapamiento con la barra de estado
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Asignación de referencias de UI
        etUsuario = findViewById(R.id.etUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);
    }

    // Acción del botón "Registrarse"
    public void registrarUsuario(View view) {
        // Obtención y limpieza de campos
        String usuario = etUsuario.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmarPassword = etConfirmarPassword.getText().toString().trim();

        // Validación de campos vacíos
        if (TextUtils.isEmpty(usuario) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmarPassword)) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de contraseñas
        if (!password.equals(confirmarPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Usuario con los datos introducidos
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(usuario);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setContrasena(password);

        // Enviar petición de registro al backend
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.registrarUsuario(nuevoUsuario);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Registro OK → redirigir a login
                    Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Error de backend
                    Toast.makeText(RegistroActivity.this, "Error al registrar. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                    Log.e("Registro", "Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Error de red (timeout, sin conexión, etc)
                Toast.makeText(RegistroActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Registro", "Fallo en la llamada", t);
            }
        });
    }

    // Navegación hacia pantalla de login
    public void irAIniciarSesion(View view) {
        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
        startActivity(intent);
    }
}




