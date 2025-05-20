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
    private int idUsuario;
    private EditText etCodigoGrupo;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.unirse_equipo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.unirse), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etCodigoGrupo = findViewById(R.id.etCodigoGrupo);

        idUsuario = getIntent().getIntExtra("idUsuario", -1); // -1 como fallback

        if (idUsuario == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            finish();
        }

        apiService = RetrofitClient.getApiService();

    }

    public void unirseAEquipo(View view) {

        String codigoGrupo = etCodigoGrupo.getText().toString().trim();

        if (codigoGrupo.isEmpty()) {
            Toast.makeText(this, "Introduce el código del grupo", Toast.LENGTH_SHORT).show();
            return;
        }

        UnirseEquipoDTO dto = new UnirseEquipoDTO(idUsuario, codigoGrupo);

        Call<Void> call = apiService.unirseAEquipo(dto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UnirseEquipoActivity.this, "Te has unido al equipo correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UnirseEquipoActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UnirseEquipoActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UnirseEquipoActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
