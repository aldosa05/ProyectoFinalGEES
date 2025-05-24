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

    private View filaAzul, filaRoja, filaBalon;
    private Button btnConfirmar;

    public interface FichaConfigListener {
        void onConfigurarFichas(int azules, int rojas, int balones);
    }

    private FichaConfigListener listener;

    public void setFichaConfigListener(FichaConfigListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet_config_fichas, container, false);

        ViewGroup layoutFichas = view.findViewById(R.id.layoutFichas);

        filaAzul = layoutFichas.getChildAt(0);
        filaRoja = layoutFichas.getChildAt(1);
        filaBalon = layoutFichas.getChildAt(2);

        configurarNombreFicha(filaAzul, "Ficha Azul");
        configurarNombreFicha(filaRoja, "Ficha Roja");
        configurarNombreFicha(filaBalon, "Balón");

        configurarContador(filaAzul, 11);
        configurarContador(filaRoja, 11);
        configurarContador(filaBalon, 4); // máximo 4 balones

        btnConfirmar = view.findViewById(R.id.btnCerrar);
        btnConfirmar.setOnClickListener(v -> {
            int azules = obtenerCantidad(filaAzul);
            int rojas = obtenerCantidad(filaRoja);
            int balones = obtenerCantidad(filaBalon);

            if (listener != null) {
                listener.onConfigurarFichas(azules, rojas, balones);
            }
            dismiss();
        });

        return view;
    }

    private void configurarContador(View filaFicha, int max) {
        TextView tvCantidad = filaFicha.findViewById(R.id.tvCantidad);
        Button btnSumar = filaFicha.findViewById(R.id.btnSumar);
        Button btnRestar = filaFicha.findViewById(R.id.btnRestar);

        btnSumar.setOnClickListener(v -> {
            int actual = Integer.parseInt(tvCantidad.getText().toString());
            if (actual < max) {
                tvCantidad.setText(String.valueOf(actual + 1));
            }
        });

        btnRestar.setOnClickListener(v -> {
            int actual = Integer.parseInt(tvCantidad.getText().toString());
            if (actual > 0) {
                tvCantidad.setText(String.valueOf(actual - 1));
            }
        });
    }

    private int obtenerCantidad(View filaFicha) {
        TextView tvCantidad = filaFicha.findViewById(R.id.tvCantidad);
        return Integer.parseInt(tvCantidad.getText().toString());
    }

    private void configurarNombreFicha(View filaFicha, String nombreFicha) {
        TextView tvTipoFicha = filaFicha.findViewById(R.id.tvTipoFicha);
        tvTipoFicha.setText(nombreFicha);
    }


}
