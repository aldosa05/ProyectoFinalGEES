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

    private ImageView pistaImage;
    private Button btnConfigurar;
    private ConstraintLayout rootLayout;
    private String deporte;

    public PizarraFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pizarra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pistaImage = view.findViewById(R.id.pista_image);
        btnConfigurar = view.findViewById(R.id.btn_config_pizarra);
        rootLayout = view.findViewById(R.id.pizarra_root);

        if (getArguments() != null) {
            deporte = getArguments().getString("deporte", "Futbol"); // por defecto
        }

        Log.d("PizarraFragment", "🔑 Deporte: " + deporte);

        cargarImagenCampo();

        btnConfigurar.setOnClickListener(v -> {
            FichaConfigBottomSheet bottomSheet = new FichaConfigBottomSheet();
            bottomSheet.setFichaConfigListener(this); // 👉 importante
            bottomSheet.show(getParentFragmentManager(), "ficha_config");
        });
    }

    private void cargarImagenCampo() {
        int resId;

        if ("Futbol".equalsIgnoreCase(deporte)) {
            resId = R.drawable.campo_futbol;
        } else if ("Futbol sala".equalsIgnoreCase(deporte)) {
            resId = R.drawable.campo_futsal;
        } else if ("Baloncesto".equalsIgnoreCase(deporte)) {
            resId = R.drawable.campo_baloncesto;
        } else {
            resId = R.drawable.ic_launcher_background;
        }

        pistaImage.setImageResource(resId);
    }

    @Override
    public void onConfigurarFichas(int azules, int rojas, int balones) {
        // 🧹 Limpia las fichas actuales (sin tocar imagen ni botón)
        for (int i = rootLayout.getChildCount() - 1; i >= 0; i--) {
            View child = rootLayout.getChildAt(i);
            if (child != pistaImage && child != btnConfigurar) {
                rootLayout.removeViewAt(i);
            }
        }

        // 🔁 Agrega nuevas fichas
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
            // ⚽ Balón con imagen
            ImageView balon = new ImageView(requireContext());
            balon.setImageResource(R.drawable.ic_balon); // ← Asegúrate de tener esta imagen
            balon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            balon.setBackground(null);
            ficha = balon;
        } else {
            // 🔵 🔴 Fichas redondas con color
            View circulo = new View(requireContext());
            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.OVAL);
            bg.setColor(tipo);
            circulo.setBackground(bg);
            ficha = circulo;
        }

        ficha.setLayoutParams(params);
        ficha.setX((float) (Math.random() * (rootLayout.getWidth() - size)));
        ficha.setY((float) (Math.random() * (rootLayout.getHeight() - size - 200)));

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
