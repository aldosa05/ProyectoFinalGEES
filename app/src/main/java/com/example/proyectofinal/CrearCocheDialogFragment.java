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

    // Interface para comunicar el resultado al fragmento o actividad que lo invoque
    public interface CrearCocheListener {
        void onCrearCoche(String nombreConductor, int numeroPlazas);
    }

    private CrearCocheListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 🧠 Intenta obtener el listener desde el contexto (actividad) o el parent fragment
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
        // Inflamos el layout personalizado del diálogo
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_crear_coche, null);

        EditText etNombre = view.findViewById(R.id.etNombreConductor);
        EditText etPlazas = view.findViewById(R.id.etNumeroPlazas);

        // Creamos el diálogo con botones
        return new AlertDialog.Builder(requireContext())
                .setTitle("Crear nuevo coche")
                .setView(view)
                // ✅ Acción de crear coche
                .setPositiveButton("Crear", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String plazasStr = etPlazas.getText().toString().trim();

                    // Validación simple de campos vacíos
                    if (nombre.isEmpty() || plazasStr.isEmpty()) {
                        Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Validación de número de plazas como entero
                    int plazas;
                    try {
                        plazas = Integer.parseInt(plazasStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Número inválido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Emitimos resultado al listener
                    listener.onCrearCoche(nombre, plazas);
                })
                // ❌ Botón de cancelar, sin lógica extra
                .setNegativeButton("Cancelar", null)
                .create();
    }

}
