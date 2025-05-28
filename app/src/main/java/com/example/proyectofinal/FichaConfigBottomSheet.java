package com.example.proyectofinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FichaConfigBottomSheet extends BottomSheetDialogFragment {

    // 🔵 Elementos visuales para cada tipo de ficha
    private View filaAzul, filaRoja, filaBalon;
    private Button btnConfirmar;

    // 📣 Callback para enviar los valores seleccionados al fragmento/padre
    public interface FichaConfigListener {
        void onConfigurarFichas(int azules, int rojas, int balones);
    }

    private FichaConfigListener listener;

    // 🎯 Setter para el listener externo
    public void setFichaConfigListener(FichaConfigListener listener) {
        this.listener = listener;
    }

    // 🧱 Inflado del layout del BottomSheet
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet_config_fichas, container, false);

        // 📦 Layout padre que contiene las filas de configuración
        ViewGroup layoutFichas = view.findViewById(R.id.layoutFichas);

        // 🧩 Obtenemos referencias individuales a cada fila
        filaAzul = layoutFichas.getChildAt(0);
        filaRoja = layoutFichas.getChildAt(1);
        filaBalon = layoutFichas.getChildAt(2);

        // 🏷 Asignamos nombre visible a cada tipo de ficha
        configurarNombreFicha(filaAzul, "Ficha Azul");
        configurarNombreFicha(filaRoja, "Ficha Roja");
        configurarNombreFicha(filaBalon, "Balón");

        // 🔢 Configuramos contadores con sus límites máximos
        configurarContador(filaAzul, 11);  // Máximo de fichas azules
        configurarContador(filaRoja, 11);  // Máximo de fichas rojas
        configurarContador(filaBalon, 4);  // Máximo de 4 balones

        // ✅ Botón que guarda los valores y notifica al listener
        btnConfirmar = view.findViewById(R.id.btnCerrar);
        btnConfirmar.setOnClickListener(v -> {
            int azules = obtenerCantidad(filaAzul);
            int rojas = obtenerCantidad(filaRoja);
            int balones = obtenerCantidad(filaBalon);

            if (listener != null) {
                listener.onConfigurarFichas(azules, rojas, balones);
            }

            // ⛔ Cerramos el BottomSheet
            dismiss();
        });

        return view;
    }

    // 🔄 Lógica para sumar/restar la cantidad de fichas
    private void configurarContador(View filaFicha, int max) {
        TextView tvCantidad = filaFicha.findViewById(R.id.tvCantidad);
        Button btnSumar = filaFicha.findViewById(R.id.btnSumar);
        Button btnRestar = filaFicha.findViewById(R.id.btnRestar);

        // ➕ Aumentar cantidad hasta el máximo permitido
        btnSumar.setOnClickListener(v -> {
            int actual = Integer.parseInt(tvCantidad.getText().toString());
            if (actual < max) {
                tvCantidad.setText(String.valueOf(actual + 1));
            }
        });

        // ➖ Disminuir cantidad hasta mínimo cero
        btnRestar.setOnClickListener(v -> {
            int actual = Integer.parseInt(tvCantidad.getText().toString());
            if (actual > 0) {
                tvCantidad.setText(String.valueOf(actual - 1));
            }
        });
    }

    // 📥 Obtener la cantidad actual desde el TextView de la fila
    private int obtenerCantidad(View filaFicha) {
        TextView tvCantidad = filaFicha.findViewById(R.id.tvCantidad);
        return Integer.parseInt(tvCantidad.getText().toString());
    }

    // 🏷 Asignar texto de tipo de ficha (Azul, Roja, Balón)
    private void configurarNombreFicha(View filaFicha, String nombreFicha) {
        TextView tvTipoFicha = filaFicha.findViewById(R.id.tvTipoFicha);
        tvTipoFicha.setText(nombreFicha);
    }
}

