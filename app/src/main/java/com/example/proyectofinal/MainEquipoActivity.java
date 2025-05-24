package com.example.proyectofinal;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;

public class MainEquipoActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;
    int idEquipo;
    String rol;

    String deporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_equipo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainEquipoAct), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        idEquipo = getIntent().getIntExtra("idEquipo", -1);
        Log.d("MainEquipoActivity", "🔑 idEquipo: " + idEquipo);
        deporte = getIntent().getStringExtra("deporte");
        Log.d("MainEquipoActivity", "🔑 Llega deporte: " + deporte);
        rol = getIntent().getStringExtra("rol");
        Menu menu = navView.getMenu();

        if ("entrenador".equals(rol)) {
            // El entrenador ve todo
        } else if ("jugador".equals(rol)) {
            // Ocultar opciones solo para entrenadores
            menu.findItem(R.id.nav_asistencia).setVisible(false);
            menu.findItem(R.id.nav_pizarra).setVisible(false);
        } else if ("familiar".equals(rol)) {
            // Ocultar opciones que no aplican a familiares
            menu.findItem(R.id.nav_asistencia).setVisible(false);
            menu.findItem(R.id.nav_pizarra).setVisible(false);

        }


        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            HorarioFragment fragment = new HorarioFragment();

            Bundle args = new Bundle();
            args.putInt("idEquipo", idEquipo);
            args.putString("rol", rol);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            navView.setCheckedItem(R.id.nav_horario);
        }

        navView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_horario) {
                HorarioFragment fragment = new HorarioFragment();

                Bundle args = new Bundle();
                args.putInt("idEquipo", idEquipo);
                args.putString("rol", rol);
                fragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            } else if (itemId == R.id.nav_convocatoria) {
                Toast.makeText(this, "Convocatoria próximamente", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_coches) {
                Toast.makeText(this, "Coches próximamente", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_multas) {
                Toast.makeText(this, "Multas próximamente", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_salir) {
                finish();
            } else if (itemId == R.id.nav_eliminar) {
                Toast.makeText(this, "Equipo eliminado (TODO)", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_pizarra) {

            PizarraFragment pizarraFragment = new PizarraFragment();

            Bundle args = new Bundle();
            args.putString("deporte", deporte);
            Log.d("MainEquipoActivity", "🔑 Envio deporte: " + deporte);// pásale el deporte que recibiste
            pizarraFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, pizarraFragment)
                    .commit();
        }


        drawerLayout.closeDrawers();
            return true;
        });

    }

}
