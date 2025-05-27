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

    public interface OnMultaCreadaListener {
        void onMultaCreada(Multa multa);
    }

    private OnMultaCreadaListener listener;

    private EditText etMotivo, etMonto;
    private Button btnFecha, btnGuardar;
    private LocalDate fechaSeleccionada;

    public CrearMultaDialogFragment(OnMultaCreadaListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_crear_multa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etMotivo = view.findViewById(R.id.etMotivo);
        etMonto = view.findViewById(R.id.etMonto);
        btnFecha = view.findViewById(R.id.btnFechaMulta);
        btnGuardar = view.findViewById(R.id.btnGuardarMulta);

        btnFecha.setOnClickListener(v -> mostrarDatePicker());

        btnGuardar.setOnClickListener(v -> {
            String motivo = etMotivo.getText().toString();
            String montoStr = etMonto.getText().toString();

            if (motivo.isEmpty() || montoStr.isEmpty() || fechaSeleccionada == null) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double monto = Double.parseDouble(montoStr);

            Multa nueva = new Multa();
            nueva.setMotivo(motivo);
            nueva.setMonto(monto);
            nueva.setFechaAsignacion(fechaSeleccionada.toString()); // YYYY-MM-DD
            nueva.setPagada(false);

            listener.onMultaCreada(nueva);
            dismiss();
        });
    }

    private void mostrarDatePicker() {
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona una fecha")
                .build();

        picker.addOnPositiveButtonClickListener(selection -> {
            Instant instant = Instant.ofEpochMilli(selection);
            fechaSeleccionada = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            btnFecha.setText("📅 " + fechaSeleccionada.toString());
        });

        picker.show(getParentFragmentManager(), "FECHA_MULTA");
    }
}

