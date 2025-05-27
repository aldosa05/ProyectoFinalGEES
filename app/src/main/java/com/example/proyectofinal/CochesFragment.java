package com.example.proyectofinal;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 🎯 Este fragmento muestra y gestiona los coches con sus ocupantes
public class CochesFragment extends Fragment
        implements CrearCocheDialogFragment.CrearCocheListener,
        AnadirOcupanteDialogFragment.AnadirOcupanteListener,
        CocheAdapter.OnCocheClickListener {

    // 🧱 Vistas y variables principales
    private RecyclerView recyclerViewCoches;
    private Button btnAñadirCoche;
    private CocheAdapter adapter;

    // 🚘 Lista que contiene los coches con su conductor y ocupantes
    private List<CocheConOcupantesDTO> listaCoches = new ArrayList<>();

    // 📦 Datos del usuario que llegan como argumentos
    private String rol;
    private String categoria;

    private int idEquipo;

    // 📌 Para controlar a qué coche le añadiremos ocupantes desde el diálogo
    private int cocheSeleccionadoId = -1;
    private int cocheSeleccionadoPos = -1;

    public CochesFragment() {
        // Constructor vacío requerido por Fragment
    }

    // 📦 Inflamos el layout XML del fragmento
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coche, container, false);
    }

    // 🔁 Aquí inicializamos la vista y configuramos el comportamiento del fragmento
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().setTitle("Coches");

        recyclerViewCoches = view.findViewById(R.id.recyclerViewCoches);
        btnAñadirCoche = view.findViewById(R.id.btnAñadirCoche);

        // 🔧 Configuramos el RecyclerView con Layout vertical
        recyclerViewCoches.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 🔗 Creamos el adapter y le pasamos el listener que somos nosotros mismos
        adapter = new CocheAdapter(listaCoches, this);
        recyclerViewCoches.setAdapter(adapter);

        // 📥 Recibimos los argumentos pasados al fragmento (rol y categoría)
        if (getArguments() != null) {
            rol = getArguments().getString("rol", "");
            categoria = getArguments().getString("categoria", "");
            idEquipo = getArguments().getInt("idEquipo", -1);
        }

        // 👀 Mostramos u ocultamos el botón dependiendo del rol
        if ("entrenador".equalsIgnoreCase(rol) || "familiar".equalsIgnoreCase(rol)) {
            btnAñadirCoche.setVisibility(View.VISIBLE);
            recyclerViewCoches.setVisibility(View.VISIBLE);
        } else if ("jugador".equalsIgnoreCase(rol) && "senior".equalsIgnoreCase(categoria)) {
            btnAñadirCoche.setVisibility(View.GONE);
            recyclerViewCoches.setVisibility(View.VISIBLE);
        } else {
            btnAñadirCoche.setVisibility(View.GONE);
            recyclerViewCoches.setVisibility(View.GONE);
        }

        // ➕ Al pulsar, abrimos el diálogo para crear coche
        btnAñadirCoche.setOnClickListener(v -> crearCoche());

        // 🔄 Cargamos los coches desde el backend
        cargarCoches();
    }

    // 🔄 Obtener lista de coches + ocupantes del backend
    private void cargarCoches() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.obtenerCochesPorEquipo(idEquipo).enqueue(new Callback<List<CocheConOcupantesDTO>>() {
            @Override
            public void onResponse(Call<List<CocheConOcupantesDTO>> call, Response<List<CocheConOcupantesDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCoches.clear();
                    listaCoches.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error al cargar coches", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CocheConOcupantesDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // ➕ Mostrar diálogo para crear nuevo coche
    private void crearCoche() {
        CrearCocheDialogFragment dialog = new CrearCocheDialogFragment();
        dialog.show(getChildFragmentManager(), "crearCoche");
    }

    // 🔁 Callback que viene del diálogo de crear coche
    @Override
    public void onCrearCoche(String nombreConductor, int numeroPlazas) {
        Coche nuevo = new Coche();
        nuevo.setNombreConductor(nombreConductor);
        nuevo.setNumeroPlazas(numeroPlazas);
        nuevo.setIdEquipo(idEquipo);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.crearCoche(nuevo).enqueue(new Callback<Coche>() {
            @Override
            public void onResponse(Call<Coche> call, Response<Coche> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Coche creado correctamente", Toast.LENGTH_SHORT).show();
                    cargarCoches();
                } else {
                    Toast.makeText(getContext(), "Error al crear coche", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Coche> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ➕ Al pulsar el botón verde en un coche para añadir pasajero
    @Override
    public void onAgregarPasajero(int position) {
        CocheConOcupantesDTO coche = listaCoches.get(position);

        // ❌ Si ya está lleno, no se puede añadir más
        if (coche.getOcupantes().size() >= coche.getNumeroPlazas()) {
            Toast.makeText(getContext(), "Este coche ya está lleno", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🧭 Guardamos el ID del coche para usarlo después
        cocheSeleccionadoId = coche.getId();
        cocheSeleccionadoPos = position;

        // 📦 Abrimos el diálogo para escribir el nombre del ocupante
        AnadirOcupanteDialogFragment dialog = new AnadirOcupanteDialogFragment();
        dialog.show(getChildFragmentManager(), "dialog_ocupante");
    }

    // 🔁 Callback que viene del diálogo para añadir pasajero
    @Override
    public void onNombreOcupanteConfirmado(String nombre) {
        if (cocheSeleccionadoId == -1) return;

        OcupanteCoche ocupante = new OcupanteCoche();
        ocupante.setIdCoche(cocheSeleccionadoId);
        ocupante.setNombreOcupante(nombre);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.anadirOcupante(ocupante).enqueue(new Callback<OcupanteCoche>() {
            @Override
            public void onResponse(Call<OcupanteCoche> call, Response<OcupanteCoche> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Ocupante añadido", Toast.LENGTH_SHORT).show();
                    cargarCoches(); // 🧼 Refrescamos lista para ver el cambio
                } else {
                    Toast.makeText(getContext(), "Error al añadir ocupante", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OcupanteCoche> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de red", Toast.LENGTH_SHORT).show();
            }
        });

        cocheSeleccionadoId = -1;
        cocheSeleccionadoPos = -1;
    }

    // 🗑️ Eliminar un coche por ID
    private void eliminarCoche(int idCoche, int pos) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.eliminarCoche(idCoche).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listaCoches.remove(pos);
                    adapter.notifyItemRemoved(pos);
                } else {
                    Toast.makeText(getContext(), "Error al eliminar coche", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onEliminarCoche(int position) {
        eliminarCoche(listaCoches.get(position).getId(), position);
    }


}
