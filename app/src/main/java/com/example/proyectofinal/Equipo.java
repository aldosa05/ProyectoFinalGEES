package com.example.proyectofinal;

import com.google.gson.annotations.SerializedName;

public class Equipo {
    @SerializedName("id_equipo")
    private int idEquipo;

    @SerializedName("nombre_equipo")
    private String nombreEquipo;

    private String categoria;
    private String deporte;

    @SerializedName("tiene_multas")
    private boolean tieneMultas;

    // Si tu backend ya empuja los códigos al JSON:
    @SerializedName("codigo_grupo_entrenador")
    private String codigoGrupoEntrenador;
    @SerializedName("codigo_grupo_jugador")
    private String codigoGrupoJugador;
    @SerializedName("codigo_grupo_familiar")
    private String codigoGrupoFamiliar;

    public Equipo() {}

    // Constructor para envío (sin id ni códigos generados)
    public Equipo(String nombreEquipo, String categoria, String deporte, boolean tieneMultas) {
        this.nombreEquipo = nombreEquipo;
        this.categoria   = categoria;
        this.deporte     = deporte;
        this.tieneMultas = tieneMultas;
    }

    // Getters & setters
    public int getIdEquipo() { return idEquipo; }
    public void setIdEquipo(int idEquipo) { this.idEquipo = idEquipo; }

    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDeporte() { return deporte; }
    public void setDeporte(String deporte) { this.deporte = deporte; }

    public boolean isTieneMultas() { return tieneMultas; }
    public void setTieneMultas(boolean tieneMultas) { this.tieneMultas = tieneMultas; }

    public String getCodigoGrupoEntrenador() { return codigoGrupoEntrenador; }
    public void setCodigoGrupoEntrenador(String codigoGrupoEntrenador) {
        this.codigoGrupoEntrenador = codigoGrupoEntrenador;
    }
    public String getCodigoGrupoJugador() { return codigoGrupoJugador; }
    public void setCodigoGrupoJugador(String codigoGrupoJugador) {
        this.codigoGrupoJugador = codigoGrupoJugador;
    }
    public String getCodigoGrupoFamiliar() { return codigoGrupoFamiliar; }
    public void setCodigoGrupoFamiliar(String codigoGrupoFamiliar) {
        this.codigoGrupoFamiliar = codigoGrupoFamiliar;
    }
}
