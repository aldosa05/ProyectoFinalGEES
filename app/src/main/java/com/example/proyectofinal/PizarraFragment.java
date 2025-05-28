package com.example.proyectofinal;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class PizarraFragment extends Fragment implements FichaConfigBottomSheet.FichaConfigListener {

    // Vistas de la UI
    private ImageView pistaImage;
    private Button btnConfigurar;
    private ConstraintLayout rootLayout;

    // Tipo de deporte que se usará para cargar la pista adecuada
    private String deporte;

    public PizarraFragment() {
        // Constructor vacío obligatorio para Fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pizarra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        requireActivity().setTitle("Pizarra");

        // Referencias a componentes del layout
        pistaImage = view.findViewById(R.id.pista_image);
        btnConfigurar = view.findViewById(R.id.btn_config_pizarra);
        rootLayout = view.findViewById(R.id.pizarra_root);

        // Obtener el tipo de deporte (por defecto: Futbol)
        if (getArguments() != null) {
            deporte = getArguments().getString("deporte", "Futbol");
        }

        Log.d("PizarraFragment", "🔑 Deporte: " + deporte);

        // Carga la imagen del campo de juego según el deporte
        cargarImagenCampo();

        // Abre bottom sheet para seleccionar fichas
        btnConfigurar.setOnClickListener(v -> {
            FichaConfigBottomSheet bottomSheet = new FichaConfigBottomSheet();
            bottomSheet.setFichaConfigListener(this); // Comunicación Fragment <-> BottomSheet
            bottomSheet.show(getParentFragmentManager(), "ficha_config");
        });
    }

    private void cargarImagenCampo() {
        // Switch manual por string para setear imagen según deporte
        int resId;
        if ("Futbol".equalsIgnoreCase(deporte)) {
            resId = R.drawable.campo_futbol;
        } else if ("Futbol sala".equalsIgnoreCase(deporte)) {
            resId = R.drawable.campo_futsal;
        } else if ("Baloncesto".equalsIgnoreCase(deporte)) {
            resId = R.drawable.campo_baloncesto;
        } else {
            resId = R.drawable.ic_launcher_background; // Fallback visual
        }

        pistaImage.setImageResource(resId);
    }

    // Callback que se ejecuta al cerrar el BottomSheet con selección de fichas
    @Override
    public void onConfigurarFichas(int azules, int rojas, int balones) {
        // Elimina todas las fichas anteriores, pero no borra imagen ni botón
        for (int i = rootLayout.getChildCount() - 1; i >= 0; i--) {
            View child = rootLayout.getChildAt(i);
            if (child != pistaImage && child != btnConfigurar) {
                rootLayout.removeViewAt(i);
            }
        }

        // Crea y agrega las fichas solicitadas
        for (int i = 0; i < azules; i++) agregarFicha(Color.BLUE);
        for (int i = 0; i < rojas; i++) agregarFicha(Color.RED);
        for (int i = 0; i < balones; i++) agregarFicha(Color.BLACK);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void agregarFicha(int tipo) {
        View ficha;
        int size = 100;
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(size, size);

        if (tipo == Color.BLACK) {
            // Balón → imagen personalizada
            ImageView balon = new ImageView(requireContext());
            balon.setImageResource(R.drawable.ic_balon); // Asegúrate de tener este recurso
            balon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            balon.setBackground(null);
            ficha = balon;
        } else {
            // Jugadores → círculos de color (azul/rojo)
            View circulo = new View(requireContext());
            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.OVAL);
            bg.setColor(tipo);
            circulo.setBackground(bg);
            ficha = circulo;
        }

        ficha.setLayoutParams(params);

        // Posición inicial aleatoria en el área visible (ajustada para evitar solapamiento con botones)
        ficha.setX((float) (Math.random() * (rootLayout.getWidth() - size)));
        ficha.setY((float) (Math.random() * (rootLayout.getHeight() - size - 200)));

        // Drag & drop con touch listener
        ficha.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        v.setX(event.getRawX() + dX);
                        v.setY(event.getRawY() + dY);
                        return true;
                }
                return false;
            }
        });

        rootLayout.addView(ficha);
    }
}

