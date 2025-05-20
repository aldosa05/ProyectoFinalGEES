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

    private EditText etUsuario, etPassword;
    Usuario loginUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de los campos y botones
        etUsuario  = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);

    }

    public void iniciarSesion(View view) {

        String correo = etUsuario.getText().toString().trim();
        String contrasena = etPassword.getText().toString().trim();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("LOGIN", "🟢 Botón pulsado correctamente");
        Log.d("LOGIN", "📧 Correo introducido: " + correo);
        Log.d("LOGIN", "🔒 Contraseña introducida: " + contrasena);

        loginUser = new Usuario();
        loginUser.setCorreo(correo);
        loginUser.setContrasena(contrasena);

        ApiService api = RetrofitClient.getApiService();
        api.login(loginUser).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
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
                    Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "Correo no registrado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error inesperado: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void irARegistro(View view) {
        Intent intent = new Intent(MainActivity.this, RegistroActivity.class); // Redirige a la actividad de registro
        startActivity(intent);
    }

    public void recuperarContrasena(View view) {
        Intent intent = new Intent(MainActivity.this, CambiarContrasenaActivity.class); // Redirige a la actividad de registro
        startActivity(intent);
    }
}