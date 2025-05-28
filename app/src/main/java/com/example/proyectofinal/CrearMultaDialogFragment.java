package com.example.proyectofinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class CrearMultaDialogFragment extends DialogFragment {

    // 🧩 Callback que se invoca cuando se crea una multa correctamente
    public interface OnMultaCreadaListener {
        void onMultaCreada(Multa multa);
    }

    private OnMultaCreadaListener listener;

    // 📥 Inputs y botones de UI
    private EditText etMotivo, etMonto, etNombreJugador ;
    private Button btnFecha, btnGuardar;
    // 🗓 Fecha seleccionada vía MaterialDatePicker
    private LocalDate fechaSeleccionada;

    // 🏗 Constructor obligatorio que recibe el listener
    public CrearMultaDialogFragment(OnMultaCreadaListener listener) {
        this.listener = listener;
    }

    // 📐 Inflamos el layout desde XML
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_crear_multa, container, false);
    }

    // 🔌 Configuramos eventos y lógica del formulario
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 🧭 Referenciamos los elementos visuales del layout
        etMotivo = view.findViewById(R.id.etMotivo);
        etMonto = view.findViewById(R.id.etMonto);
        etNombreJugador = view.findViewById(R.id.etNombreJugador);
        btnFecha = view.findViewById(R.id.btnFechaMulta);
        btnGuardar = view.findViewById(R.id.btnGuardarMulta);

        // 🗓 Selector de fecha
        btnFecha.setOnClickListener(v -> mostrarDatePicker());

        // 💾 Validación + Creación de objeto Multa
        btnGuardar.setOnClickListener(v -> {
            String motivo = etMotivo.getText().toString();
            String montoStr = etMonto.getText().toString();
            String nombreJugador = etNombreJugador.getText().toString().trim();

            // ⚠️ Validación simple de campos obligatorios
            if (motivo.isEmpty() || montoStr.isEmpty() || fechaSeleccionada == null || nombreJugador.isEmpty() ) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔢 Conversión del monto (ya está validado que no está vacío)
            double monto = Double.parseDouble(montoStr);

            // 🛠 Instancia y asignación de valores
            Multa nueva = new Multa();
            nueva.setMotivo(motivo);
            nueva.setMonto(monto);
            nueva.setFechaAsignacion(fechaSeleccionada.toString()); // YYYY-MM-DD
            nueva.setPagada(false);
            nueva.setNombreJugador(nombreJugador);

            // 📤 Comunicación con fragmento/padre
            listener.onMultaCreada(nueva);
            // ❌ Cerramos el diálogo
            dismiss();
        });
    }

    // 📅 Componente de selección de fecha basado en MaterialDatePicker
    private void mostrarDatePicker() {
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona una fecha")
                .build();

        picker.addOnPositiveButtonClickListener(selection -> {
            // 🌍 Conversión de timestamp a LocalDate
            Instant instant = Instant.ofEpochMilli(selection);
            fechaSeleccionada = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            // 🖊️ Mostrar fecha en el botón como feedback visual
            btnFecha.setText("📅 " + fechaSeleccionada.toString());
        });

        // 🔎 Mostrar el date picker con tag para debugging
        picker.show(getParentFragmentManager(), "FECHA_MULTA");
    }
}

