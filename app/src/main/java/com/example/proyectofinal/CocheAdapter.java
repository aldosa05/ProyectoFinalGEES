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

    private List<CocheConOcupantesDTO> coches;
    private final OnCocheClickListener listener;

    public interface OnCocheClickListener {
        void onEliminarCoche(int position);
        void onAgregarPasajero(int position);
    }

    public CocheAdapter(List<CocheConOcupantesDTO> coches, OnCocheClickListener listener) {
        this.coches = coches;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CocheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        holder.btnAgregar.setOnClickListener(v -> listener.onAgregarPasajero(position));
        holder.btnCerrar.setOnClickListener(v -> listener.onEliminarCoche(position));
    }

    @Override
    public int getItemCount() {
        return coches.size();
    }

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
