package com.example.proyectofinal;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnadirConvocadoActivity extends AppCompatActivity {

    private LinearLayout layoutConvocados;
    private Button btnAddJugador, btnGuardarConvocados;

    private int idPartido;

    private final List<View> filasConvocados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_convocado);

        layoutConvocados = findViewById(R.id.layoutConvocados);
        btnAddJugador = findViewById(R.id.btnAddJugador);
        btnGuardarConvocados = findViewById(R.id.btnGuardarConvocados);

        idPartido = getIntent().getIntExtra("idPartido", -1);

        // Añadir 1 fila inicial
        agregarFilaConvocado();

        btnAddJugador.setOnClickListener(v -> agregarFilaConvocado());

        btnGuardarConvocados.setOnClickListener(v -> guardarConvocados());
    }

    private void agregarFilaConvocado() {
        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setGravity(Gravity.CENTER_VERTICAL);
        fila.setPadding(0, 8, 0, 8);

        EditText etNombre = new EditText(this);
        etNombre.setHint("Nombre");
        etNombre.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        EditText etDorsal = new EditText(this);
        etDorsal.setHint("Dorsal");
        etDorsal.setInputType(InputType.TYPE_CLASS_NUMBER);
        etDorsal.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        fila.addView(etNombre);
        fila.addView(etDorsal);

        layoutConvocados.addView(fila, layoutConvocados.getChildCount() - 2); // antes de los botones
        filasConvocados.add(fila);
    }

    private void guardarConvocados() {
        List<ConvocadoRequest> convocados = new ArrayList<>();

        for (View fila : filasConvocados) {
            LinearLayout layout = (LinearLayout) fila;
            EditText etNombre = (EditText) layout.getChildAt(0);
            EditText etDorsal = (EditText) layout.getChildAt(1);

            String nombre = etNombre.getText().toString().trim();
            String dorsalStr = etDorsal.getText().toString().trim();

            if (!nombre.isEmpty() && !dorsalStr.isEmpty()) {
                int dorsal = Integer.parseInt(dorsalStr);
                convocados.add(new ConvocadoRequest(idPartido, nombre, dorsal));
            }
        }

        if (convocados.isEmpty()) {
            Toast.makeText(this, "Debes añadir al menos un jugador", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = RetrofitClient.getApiService();
        api.insertarConvocados(convocados).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(AnadirConvocadoActivity.this, "✅ Convocados guardados", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AnadirConvocadoActivity.this, "❌ Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
