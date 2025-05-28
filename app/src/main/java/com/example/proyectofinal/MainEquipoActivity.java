package com.example.proyectofinal;

import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainEquipoActivity extends AppCompatActivity {

    // 🔗 Componentes UI principales
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;

    // 📦 Parámetros del intent
    int idEquipo;
    String rol;
    String deporte;
    String NombreEquipo;
    boolean usaMultas;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Soporte visual edge-to-edge
        setContentView(R.layout.activity_main_equipo);

        // Ajustes para evitar que la UI se solape con barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainEquipoAct), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔗 Referencias a views
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // 📥 Recuperar datos del intent
        idEquipo = getIntent().getIntExtra("idEquipo", -1);
        Log.d("MainEquipoActivity", "🔑 idEquipo: " + idEquipo);
        idUsuario = getIntent().getIntExtra("idUsuario", -1);
        Log.d("MainEquipoActivity", "🔑 idUsuario: " + idUsuario);
        deporte = getIntent().getStringExtra("deporte");
        rol = getIntent().getStringExtra("rol");
        NombreEquipo = getIntent().getStringExtra("NombreEquipo");
        Log.d("MainEquipoActivity", "🔑 NombreEquipo: " + NombreEquipo);

        Menu menu = navView.getMenu();

        // 🔍 Ajustar visibilidad según el rol
        if ("entrenador".equals(rol)) {
            // entrenador ve todo
        } else if ("jugador".equals(rol)) {
            menu.findItem(R.id.nav_pizarra).setVisible(false);
        } else if ("familiar".equals(rol)) {
            menu.findItem(R.id.nav_pizarra).setVisible(false);
        }

        // 🎯 Mostrar u ocultar opción de multas
        usaMultas = getIntent().getBooleanExtra("usaMultas", false);
        menu.findItem(R.id.nav_multas).setVisible(usaMultas);

        // 🎯 Setear la toolbar como actionbar
        setSupportActionBar(toolbar);

        // 🎛️ Setup del drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 🧩 Carga inicial: HorarioFragment
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

        // 🚦 Manejador del menú lateral
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
                ConvocatoriaFragment fragment = new ConvocatoriaFragment();
                Bundle args = new Bundle();
                args.putInt("idEquipo", idEquipo);
                args.putString("rol", rol);
                args.putString("NombreEquipo", NombreEquipo);
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            } else if (itemId == R.id.nav_coches) {
                CochesFragment fragment = new CochesFragment();
                Bundle args = new Bundle();
                args.putInt("idEquipo", idEquipo);
                args.putString("rol", rol);
                args.putString("categoria", "senior"); // puedes reemplazar por categoría real
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
                navView.setCheckedItem(R.id.nav_coches);
            } else if (itemId == R.id.nav_multas) {
                MultasFragment fragment = new MultasFragment();
                Bundle args = new Bundle();
                args.putInt("idEquipo", idEquipo);
                args.putString("rol", rol);
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
                navView.setCheckedItem(R.id.nav_multas);
            } else if (itemId == R.id.nav_salir) {
                finish(); // cierra actividad
            } else if (itemId == R.id.nav_eliminar) {
                eliminarRelacionUsuarioEquipo();
            } else if (itemId == R.id.nav_pizarra) {
                PizarraFragment fragment = new PizarraFragment();
                Bundle args = new Bundle();
                args.putString("deporte", deporte);
                Log.d("MainEquipoActivity", "🔑 Envio deporte: " + deporte);
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }

            drawerLayout.closeDrawers(); // cierra menú tras seleccionar
            return true;
        });
    }

    // 🔥 Petición DELETE para eliminar al usuario del equipo
    private void eliminarRelacionUsuarioEquipo() {
        ApiService api = RetrofitClient.getApiService();
        Log.d("MainEquipoActivity", "🔑 Eliminando idEquipo: " + idEquipo);
        Log.d("MainEquipoActivity", "🔑 Eliminando idUsuario: " + idUsuario);

        api.eliminarRelacionUsuarioEquipo(idUsuario, idEquipo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainEquipoActivity.this, "Te has salido del equipo", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainEquipoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainEquipoActivity.this, "Error al salir del equipo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainEquipoActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

