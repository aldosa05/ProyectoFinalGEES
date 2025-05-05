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

    private EditText etUsuario, etCorreo, etPassword, etConfirmarPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a los elementos del layout
        etUsuario = findViewById(R.id.etUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);

    }
/*
    public void registrarUsuario(View view) {

        String usuario = etUsuario.getText().toString();
        String email = etCorreo.getText().toString();
        String contrasena = etPassword.getText().toString();
        String confirmarContrasena = etConfirmarPassword.getText().toString();

        if (usuario.isEmpty() || email.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto Usuario con los datos del formulario
        Usuario usuarioRequest = new Usuario();
        //usuarioRequest.setUsuario(usuario);
        usuarioRequest.setCorreo(email);
        usuarioRequest.setContrasena(contrasena);

        // Llamar al servicio Retrofit para hacer la solicitud de registro
        ApiService apiService = ApiClient.getApiService();
        Call<String> call = apiService.register(usuarioRequest);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    finish();  // Regresar al login
                } else {
                    Toast.makeText(RegistroActivity.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RegistroActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}



