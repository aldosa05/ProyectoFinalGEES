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

    // Lista de multas a renderizar
    private List<Multa> multas;

    // Flag que determina si el usuario tiene permisos de entrenador
    private boolean esEntrenador;

    // Interfaz para comunicar acciones al Fragment/Activity
    private OnMultaListener listener;

    // Interface que expone acciones posibles sobre una multa
    public interface OnMultaListener {
        void onEliminarMulta(int idMulta);
        void onMarcarPagada(int idMulta);
    }

    // Constructor principal del adapter
    public MultaAdapter(List<Multa> multas, boolean esEntrenador, OnMultaListener listener) {
        this.multas = multas;
        this.esEntrenador = esEntrenador;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MultaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos layout personalizado para cada multa
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multa, parent, false);
        return new MultaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultaViewHolder holder, int position) {
        Multa multa = multas.get(position);

        // Seteo de datos al layout
        holder.tvMotivo.setText(multa.getMotivo());
        holder.tvMonto.setText("💰 Monto: " + multa.getMonto() + "€");
        holder.tvNombreJugador.setText("👤 Jugador: " + multa.getNombreJugador());
        holder.tvFecha.setText("📅 Asignada: " + multa.getFechaAsignacion());

        // Estado visual según si está pagada o no
        holder.tvEstado.setText(multa.isPagada() ? "✅ Pagada" : "⛔ Sin pagar");
        holder.tvEstado.setTextColor(multa.isPagada() ? Color.parseColor("#388E3C") : Color.RED);

        // Mostrar botones solo si el usuario es entrenador y la multa no está pagada
        if (esEntrenador && !multa.isPagada()) {
            holder.btnPagar.setVisibility(View.VISIBLE);
            holder.btnEliminar.setVisibility(View.VISIBLE);

            // Acción: marcar multa como pagada
            holder.btnPagar.setOnClickListener(v -> listener.onMarcarPagada(multa.getIdMulta()));

            // Acción: eliminar multa
            holder.btnEliminar.setOnClickListener(v -> listener.onEliminarMulta(multa.getIdMulta()));
        } else {
            // Ocultar botones si no aplica
            holder.btnPagar.setVisibility(View.GONE);
            holder.btnEliminar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return multas.size(); // Total de elementos renderizados
    }

    // ViewHolder para evitar múltiples llamadas a findViewById
    public static class MultaViewHolder extends RecyclerView.ViewHolder {
        TextView tvMotivo, tvMonto, tvFecha, tvEstado, tvNombreJugador;
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


