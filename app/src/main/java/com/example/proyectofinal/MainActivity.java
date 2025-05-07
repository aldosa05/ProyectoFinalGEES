package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
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





public class MainActivity extends AppCompatActivity {

    private EditText etUsuario, etPassword;

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
        System.out.println("🟢 Botón pulsado correctamente");
        System.out.println("📧 Correo introducido: " + correo);
        System.out.println("🔒 Contraseña introducida: " + contrasena);
        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Usuario con los datos introducidos
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);

        // Hacer la solicitud de login al backend
        ApiService apiService = RetrofitClient.getApiService();

        Call<Usuario> call = apiService.login(usuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuarioLogueado = response.body();
                    System.out.println("🟢 Login exitoso. Nombre: " + usuarioLogueado.getNombre());
                    Toast.makeText(MainActivity.this, "Bienvenido " + usuarioLogueado.getNombre(), Toast.LENGTH_SHORT).show();

                    // Ir a la siguiente pantalla
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("LOGIN", "🔴 Fallo login.");
                    Log.d("LOGIN", "Código HTTP: " + response.code());
                    Log.d("LOGIN", "Body es null: " + (response.body() == null));
                    Toast.makeText(MainActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
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