package com.example.proyectofinal;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CocheAdapter extends RecyclerView.Adapter<CocheAdapter.CocheViewHolder> {

    private List<CocheConOcupantesDTO> coches;// Lista de coches a mostrar
    private final OnCocheClickListener listener;// Interface para manejar clicks

    // Interface para delegar acciones (botones de eliminar/agregar)
    public interface OnCocheClickListener {
        void onEliminarCoche(int position);
        void onAgregarPasajero(int position);
    }
    // Constructor que recibe la lista y el listener de eventos
    public CocheAdapter(List<CocheConOcupantesDTO> coches, OnCocheClickListener listener) {
        this.coches = coches;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CocheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout XML de cada item del RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coche, parent, false);
        return new CocheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocheViewHolder holder, int position) {
        CocheConOcupantesDTO coche = coches.get(position);
        Context context = holder.itemView.getContext();

        // Setear nombre del conductor
        holder.tvTitulo.setText("Coche de " + coche.getNombreConductor() + ":");

        // Limpiar layout y volver a inflar ocupantes
        holder.layoutPasajeros.removeViews(1, holder.layoutPasajeros.getChildCount() - 1);
        // Agrega dinámicamente los pasajeros al LinearLayout
        for (String ocupante : coche.getOcupantes()) {
            TextView tvPasajero = new TextView(context);
            tvPasajero.setText("- " + ocupante);
            tvPasajero.setTextSize(16f);
            tvPasajero.setTypeface(null, Typeface.BOLD);
            tvPasajero.setTextColor(context.getResources().getColor(android.R.color.black));
            tvPasajero.setPadding(0, 4, 0, 4);
            holder.layoutPasajeros.addView(tvPasajero);
        }

        // Acciones botones
        // Botón ➕ agrega pasajero
        holder.btnAgregar.setOnClickListener(v -> listener.onAgregarPasajero(position));
        // Botón ❌ elimina coche
        holder.btnCerrar.setOnClickListener(v -> listener.onEliminarCoche(position));
    }

    @Override
    public int getItemCount() {
        return coches.size();
    }

    // ViewHolder que guarda referencias a los elementos del layout item_coche
    public static class CocheViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo;
        LinearLayout layoutPasajeros;
        Button btnAgregar;
        Button btnCerrar;

        public CocheViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            layoutPasajeros = itemView.findViewById(R.id.contenido);
            btnAgregar = itemView.findViewById(R.id.btnAgregar);
            btnCerrar = itemView.findViewById(R.id.btnCerrar);
        }
    }
}
