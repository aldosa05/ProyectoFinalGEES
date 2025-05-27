package com.example.proyectofinal;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CrearCocheDialogFragment extends DialogFragment {

    public interface CrearCocheListener {
        void onCrearCoche(String nombreConductor, int numeroPlazas);
    }

    private CrearCocheListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CrearCocheListener) {
            listener = (CrearCocheListener) context;
        } else if (getParentFragment() instanceof CrearCocheListener) {
            listener = (CrearCocheListener) getParentFragment();
        } else {
            throw new RuntimeException("Debe implementar CrearCocheListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_crear_coche, null);

        EditText etNombre = view.findViewById(R.id.etNombreConductor);
        EditText etPlazas = view.findViewById(R.id.etNumeroPlazas);

        return new AlertDialog.Builder(requireContext())
                .setTitle("Crear nuevo coche")
                .setView(view)
                .setPositiveButton("Crear", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String plazasStr = etPlazas.getText().toString().trim();

                    if (nombre.isEmpty() || plazasStr.isEmpty()) {
                        Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int plazas;
                    try {
                        plazas = Integer.parseInt(plazasStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Número inválido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    listener.onCrearCoche(nombre, plazas);
                })
                .setNegativeButton("Cancelar", null)
                .create();
    }

}
