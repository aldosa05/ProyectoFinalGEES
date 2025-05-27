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

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;
    int idEquipo;
    String rol;

    String deporte;

    String NombreEquipo;

    boolean usaMultas;
    private int idUsuario;

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
        idUsuario = getIntent().getIntExtra("idUsuario", -1);
        Log.d("MainEquipoActivity", "🔑 idEquipo: " + idUsuario);
        deporte = getIntent().getStringExtra("deporte");
        Log.d("MainEquipoActivity", "🔑 Llega deporte: " + deporte);
        rol = getIntent().getStringExtra("rol");
        NombreEquipo = getIntent().getStringExtra("NombreEquipo");
        Log.d("MainEquipoActivity", "🔑 Llega NombreEquipo: " + NombreEquipo);

        Menu menu = navView.getMenu();

        if ("entrenador".equals(rol)) {
            // El entrenador ve todo
        } else if ("jugador".equals(rol)) {
            // Ocultar opciones solo para entrenadores

            menu.findItem(R.id.nav_pizarra).setVisible(false);
        } else if ("familiar".equals(rol)) {
            // Ocultar opciones que no aplican a familiares

            menu.findItem(R.id.nav_pizarra).setVisible(false);

        }


        usaMultas = getIntent().getBooleanExtra("usaMultas", false);

        if (usaMultas) {
            menu.findItem(R.id.nav_multas).setVisible(true);
        } else {
            menu.findItem(R.id.nav_multas).setVisible(false);
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
                ConvocatoriaFragment convocatoriaFragment = new ConvocatoriaFragment();

                Bundle args = new Bundle();
                args.putInt("idEquipo", idEquipo);
                args.putString("rol", rol);
                args.putString("NombreEquipo", NombreEquipo);
                convocatoriaFragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, convocatoriaFragment)
                        .commit();
            } else if (itemId == R.id.nav_coches) {
                CochesFragment cochesFragment = new CochesFragment();

                Bundle args = new Bundle();
                args.putInt("idEquipo", idEquipo);
                args.putString("rol", rol);
                args.putString("categoria", "senior"); // ¡Usa la real si la tienes! Ej: desde login

                cochesFragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, cochesFragment)
                        .commit();

                navView.setCheckedItem(R.id.nav_coches);


            } else if (itemId == R.id.nav_multas) {
                MultasFragment multasFragment = new MultasFragment();

                Bundle args = new Bundle();
                args.putInt("idEquipo", idEquipo);
                args.putString("rol", rol);
                multasFragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, multasFragment)
                        .commit();

                navView.setCheckedItem(R.id.nav_multas);

            } else if (itemId == R.id.nav_salir) {
                finish();
            } else if (itemId == R.id.nav_eliminar) {
                eliminarRelacionUsuarioEquipo();
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


    private void eliminarRelacionUsuarioEquipo() {
        ApiService api = RetrofitClient.getApiService();

        Log.d("MainEquipoActivity", "🔑 en eliminar llega id equipo: " + idEquipo);
        Log.d("MainEquipoActivity", "🔑 en eliminar llega id usuario: " + idUsuario);

        api.eliminarRelacionUsuarioEquipo(idUsuario, idEquipo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainEquipoActivity.this, "Te has salido del equipo", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainEquipoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // vuelve a HomeActivity
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
