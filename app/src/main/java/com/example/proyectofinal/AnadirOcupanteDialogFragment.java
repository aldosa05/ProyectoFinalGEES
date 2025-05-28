package com.example.proyectofinal;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AnadirOcupanteDialogFragment extends DialogFragment {

    // Interfaz callback que debe implementar el fragmento padre para recibir el nombre ingresado
    public interface AnadirOcupanteListener {
        void onNombreOcupanteConfirmado(String nombre);
    }

    private AnadirOcupanteListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verifica que el fragmento padre implemente la interfaz requerida
        if (getParentFragment() instanceof AnadirOcupanteListener) {
            listener = (AnadirOcupanteListener) getParentFragment();
        } else {
            // Falla rápida si no se implementa el listener (mejor que fallo silencioso)
            throw new RuntimeException("El fragmento debe implementar AnadirOcupanteListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflamos el layout personalizado del diálogo
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_anadir_ocupante, null);
        EditText etNombre = view.findViewById(R.id.etNombreOcupante);
        // Construimos el AlertDialog con vista, botones y acciones
        return new AlertDialog.Builder(requireContext())
                .setTitle("Añadir pasajero")
                .setView(view)
                .setPositiveButton("Añadir", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    // Validación rápida antes de confirmar
                    if (!nombre.isEmpty()) {
                        listener.onNombreOcupanteConfirmado(nombre);// callback al fragmento
                    } else {
                        Toast.makeText(getContext(), "Nombre vacío", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)// el null cancela sin lógica adicional
                .create();
    }
}