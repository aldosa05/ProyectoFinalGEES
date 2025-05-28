package com.example.proyectofinal;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MultaAdapter extends RecyclerView.Adapter<MultaAdapter.MultaViewHolder> {

    private List<Multa> multas;
    private boolean esEntrenador;
    private OnMultaListener listener;




    public interface OnMultaListener {
        void onEliminarMulta(int idMulta);
        void onMarcarPagada(int idMulta);
    }

    public MultaAdapter(List<Multa> multas, boolean esEntrenador, OnMultaListener listener) {
        this.multas = multas;
        this.esEntrenador = esEntrenador;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MultaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multa, parent, false);
        return new MultaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultaViewHolder holder, int position) {
        Multa multa = multas.get(position);
        holder.tvMotivo.setText(multa.getMotivo());
        holder.tvMonto.setText("💰 Monto: " + multa.getMonto() + "€");
        holder.tvNombreJugador.setText("👤 Jugador: " + multa.getNombreJugador());
        holder.tvFecha.setText("📅 Asignada: " + multa.getFechaAsignacion());
        holder.tvEstado.setText(multa.isPagada() ? "✅ Pagada" : "⛔ Sin pagar");
        holder.tvEstado.setTextColor(multa.isPagada() ? Color.parseColor("#388E3C") : Color.RED);

        // Visibilidad botones
        if (esEntrenador && !multa.isPagada()) {
            holder.btnPagar.setVisibility(View.VISIBLE);
            holder.btnEliminar.setVisibility(View.VISIBLE);

            holder.btnPagar.setOnClickListener(v -> listener.onMarcarPagada(multa.getIdMulta()));
            holder.btnEliminar.setOnClickListener(v -> listener.onEliminarMulta(multa.getIdMulta()));
        } else {
            holder.btnPagar.setVisibility(View.GONE);
            holder.btnEliminar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return multas.size();
    }

    public static class MultaViewHolder extends RecyclerView.ViewHolder {
        TextView tvMotivo, tvMonto, tvFecha, tvEstado, tvNombreJugador;;
        Button btnPagar, btnEliminar;

        public MultaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMotivo = itemView.findViewById(R.id.tvMotivo);
            tvMonto = itemView.findViewById(R.id.tvMonto);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvNombreJugador = itemView.findViewById(R.id.tvNombreJugador);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            btnPagar = itemView.findViewById(R.id.btnPagar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}

