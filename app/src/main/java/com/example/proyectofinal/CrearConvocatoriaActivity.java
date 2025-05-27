package com.example.proyectofinal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearConvocatoriaActivity extends AppCompatActivity {

    private int idEquipo;
    private String nombreEquipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_convocatoria);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutCrearConvocatoria), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        idEquipo = getIntent().getIntExtra("idEquipo", -1);
        nombreEquipo = getIntent().getStringExtra("NombreEquipo");

        if (idEquipo == -1 || nombreEquipo == null) {
            Toast.makeText(this, "Faltan datos para crear la convocatoria", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView tvEquipo = findViewById(R.id.tvEquipoLocal);
        tvEquipo.setText(nombreEquipo);


        inicializarUI();
    }

    private void inicializarUI() {
        EditText etLugar = findViewById(R.id.etLugar);
        EditText etRival = findViewById(R.id.etEquipoRival);
        TextView tvFecha = findViewById(R.id.tvFechaSeleccionada);
        Button btnHoraPartido = findViewById(R.id.btnHoraPartido);
        Button btnHoraQuedada = findViewById(R.id.btnHoraQuedada);
        Button btnGuardar = findViewById(R.id.btnGuardarConvocatoria);

        // 📅 Selector de fecha
        tvFecha.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                tvFecha.setText(fecha);
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        // 🕒 Selector de hora partido
        btnHoraPartido.setOnClickListener(v -> {
            mostrarTimePicker("Hora del partido", btnHoraPartido);
        });

        // 🕒 Selector de hora quedada
        btnHoraQuedada.setOnClickListener(v -> {
            mostrarTimePicker("Hora de quedada", btnHoraQuedada);
        });

        // ✅ Botón guardar
        btnGuardar.setOnClickListener(v -> {
            String lugar = etLugar.getText().toString().trim();
            String rival = etRival.getText().toString().trim();
            String fechaOriginal = tvFecha.getText().toString().trim();
            String horaPartido = btnHoraPartido.getText().toString().trim();
            String horaQuedada = btnHoraQuedada.getText().toString().trim();

            if (lugar.isEmpty() || rival.isEmpty() || fechaOriginal.isEmpty() || horaPartido.isEmpty() || horaQuedada.isEmpty()) {
                Toast.makeText(this, "❗ Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear objeto convocatoria
            CrearConvocatoriaDTO dto = new CrearConvocatoriaDTO();
            dto.setIdEquipo(idEquipo);
            dto.setLugar(lugar);
            dto.setEquipoRival(rival);
            try {
                // Parsear desde dd/MM/yyyy a yyyy-MM-dd
                java.text.SimpleDateFormat input = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                java.text.SimpleDateFormat output = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                String fechaFormateada = output.format(input.parse(fechaOriginal));
                dto.setFecha(fechaFormateada);
            } catch (Exception e) {
                Toast.makeText(this, "❌ Error al formatear la fecha", Toast.LENGTH_SHORT).show();
                return;
            }
            dto.setHoraPartido(horaPartido);
            dto.setHoraQuedada(horaQuedada);
            Log.d("DTO-CREAR", "📤 " + new Gson().toJson(dto));


            ApiService api = RetrofitClient.getApiService();
            Call<Void> call = api.crearConvocatoria(dto);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CrearConvocatoriaActivity.this, "✅ Convocatoria creada", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CrearConvocatoriaActivity.this, "❌ Error al guardar convocatoria", Toast.LENGTH_SHORT).show();
                        Log.e("CREAR_CONVOCATORIA", "❌ Error HTTP: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CrearConvocatoriaActivity.this, "🚨 Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("CREAR_CONVOCATORIA", "🚨 Excepción", t);
                }
            });
        });
    }

    private void mostrarTimePicker(String title, Button button) {
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(18)
                .setMinute(0)
                .setTitleText(title)
                .build();

        picker.show(getSupportFragmentManager(), title);

        picker.addOnPositiveButtonClickListener(dialog -> {
            String horaFormateada = String.format(Locale.getDefault(), "%02d:%02d", picker.getHour(), picker.getMinute());
            button.setText(horaFormateada);
        });
    }
}
