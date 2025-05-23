package com.example.proyectofinal;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

public class ModificarHorarioActivity extends AppCompatActivity {

    private LinearLayout horariosContainer;
    private LayoutInflater inflater;
    private List<Horario> horarios = new ArrayList<>();

    private int idEquipo;
    private String rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar_horario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutModificarHorario), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        horariosContainer = findViewById(R.id.horariosContainer);
        inflater = LayoutInflater.from(this);

        // Recuperar datos del Intent
        idEquipo = getIntent().getIntExtra("idEquipo", -1);
        Log.d("HomeActivity", "🔑 idEquipo: " + idEquipo);

        rol = getIntent().getStringExtra("rol");

        if (idEquipo == -1 || rol == null) {
            Toast.makeText(this, "Datos insuficientes", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

    }

    public void AnyadirDiaDeEntrene(View view) {

        View horarioView = inflater.inflate(R.layout.item_horario_editor, horariosContainer, false);

        TextView tvFecha = horarioView.findViewById(R.id.tvFechaSeleccionada);
        Button btnHoraQuedada = horarioView.findViewById(R.id.btnHoraQuedada);
        Button btnHoraEntreno = horarioView.findViewById(R.id.btnHoraEntreno);

        final Calendar[] selectedDate = new Calendar[1];
        final int[] selectedHour = new int[1];
        final int[] selectedMinute = new int[1];

        tvFecha.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
                selectedDate[0] = Calendar.getInstance();
                selectedDate[0].set(year, month, dayOfMonth);
                String fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                tvFecha.setText(fecha);
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        btnHoraQuedada.setOnClickListener(v -> {
            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(18)
                    .setMinute(0)
                    .setTitleText("Selecciona hora")
                    .build();

            picker.show(getSupportFragmentManager(), "picker");

            picker.addOnPositiveButtonClickListener(dialog -> {
                selectedHour[0] = picker.getHour();
                selectedMinute[0] = picker.getMinute();
                btnHoraQuedada.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour[0], selectedMinute[0]));
            });
        });

        btnHoraEntreno.setOnClickListener(v -> {
            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(18)
                    .setMinute(0)
                    .setTitleText("Selecciona hora")
                    .build();

            picker.show(getSupportFragmentManager(), "picker");

            picker.addOnPositiveButtonClickListener(dialog -> {
                selectedHour[0] = picker.getHour();
                selectedMinute[0] = picker.getMinute();
                btnHoraEntreno.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour[0], selectedMinute[0]));
            });
        });
        horariosContainer.addView(horarioView);

    }

    public void GuardarHorarios(View view) {
        horarios.clear(); // Limpia lista previa

        int total = horariosContainer.getChildCount();
        for (int i = 0; i < total; i++) {
            View horarioView = horariosContainer.getChildAt(i);

            // Buscar views dentro del layout inflado
            EditText etLugar = horarioView.findViewById(R.id.etLugar);
            TextView tvFecha = horarioView.findViewById(R.id.tvFechaSeleccionada);
            Button btnHoraQuedada = horarioView.findViewById(R.id.btnHoraQuedada);
            Button btnHoraEntreno = horarioView.findViewById(R.id.btnHoraEntreno);


            if (etLugar == null || tvFecha == null || btnHoraQuedada == null || btnHoraEntreno == null) {
                Toast.makeText(this, "Error al leer los datos del horario #" + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }

            String fecha = tvFecha.getText().toString();
            String horaQuedada = btnHoraQuedada.getText().toString();
            String horaEntreno = btnHoraEntreno.getText().toString();
            String lugar = etLugar.getText().toString();

            if (fecha.isEmpty() || horaQuedada.isEmpty() || horaEntreno.isEmpty() || lugar.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos del horario #" + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }

            Horario horario =   new Horario(fecha, horaQuedada, horaEntreno, lugar, idEquipo);

            horarios.add(horario);
        }

        // Aquí podrías enviar la lista al backend
        Toast.makeText(this, "Se han guardado " + horarios.size() + " horarios.", Toast.LENGTH_SHORT).show();

        // Log para revisar
        for (Horario h : horarios) {
            Log.d("GUARDADO", "📅 " + h.getDia() + ", 🕓 " + h.getHora_quedada() + "/" + h.getHora_entreno() + " 📍" + h.getLugar() + " 🆔 idEquipo=" + h.getId_equipo());
        }

        Gson gson = new Gson();
        Log.d("JSON-SENT", gson.toJson(horarios));

        ApiService api = RetrofitClient.getApiService();
        Call<Void> call = api.guardarHorarios(horarios);
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ModificarHorarioActivity.this, "Horarios guardados exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ModificarHorarioActivity.this, MainEquipoActivity.class);
                    intent.putExtra("idEquipo", idEquipo);
                    intent.putExtra("rol", rol);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Vuelve atrás
                } else {
                    Toast.makeText(ModificarHorarioActivity.this, "Error al guardar horarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ModificarHorarioActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
