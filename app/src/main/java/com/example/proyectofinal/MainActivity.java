package com.example.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    // ✍️ Inputs del usuario
    private EditText etUsuario, etPassword;

    // 📦 Modelo del usuario para login
    Usuario loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // ✅ Soporte visual para Edge-to-Edge
        setContentView(R.layout.activity_main);

        // 🧱 Ajuste de padding dinámico según barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔗 Referencias a campos de texto
        etUsuario  = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
    }

    // 🚪 Acción del botón Iniciar sesión
    public void iniciarSesion(View view) {

        // 📥 Inputs del formulario
        String correo = etUsuario.getText().toString().trim();
        String contrasena = etPassword.getText().toString().trim();

        // 🔍 Validación rápida
        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // 📋 Logs de seguimiento
        Log.d("LOGIN", "🟢 Botón pulsado correctamente");
        Log.d("LOGIN", "📧 Correo introducido: " + correo);
        Log.d("LOGIN", "🔒 Contraseña introducida: " + contrasena);

        // 📦 Preparación del objeto Usuario para envío a backend
        loginUser = new Usuario();
        loginUser.setCorreo(correo);
        loginUser.setContrasena(contrasena);

        // 🌐 Llamada al backend para autenticar
        ApiService api = RetrofitClient.getApiService();
        api.login(loginUser).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // ✅ Login correcto, navegación hacia Home
                    Usuario usuario = response.body();
                    Log.d("LOGIN", "🧠 Usuario recibido: " + usuario.toString());
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("id_usuario", usuario.getId());
                    intent.putExtra("nombre_usuario", usuario.getNombre());
                    intent.putExtra("correo_usuario", usuario.getCorreo());

                    Log.d("LOGIN", "📦 Enviando a Home: " +
                            "id=" + usuario.getId() + ", nombre=" + usuario.getNombre() + ", correo=" + usuario.getCorreo());

                    startActivity(intent);

                } else if (response.code() == 401) {
                    // ❌ Contraseña incorrecta
                    Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    // ❌ Correo no registrado
                    Toast.makeText(MainActivity.this, "Correo no registrado", Toast.LENGTH_SHORT).show();
                } else {
                    // ⚠️ Otro tipo de error HTTP
                    Toast.makeText(MainActivity.this, "Error inesperado: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                // ❌ Error de red o fallo total de llamada
                Toast.makeText(MainActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 🧭 Redirige a la vista de registro
    public void irARegistro(View view) {
        Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
        startActivity(intent);
    }

    // 🔑 Redirige a pantalla de recuperación de contraseña
    public void recuperarContrasena(View view) {
        Intent intent = new Intent(MainActivity.this, CambiarContrasenaActivity.class);
        startActivity(intent);
    }
}
