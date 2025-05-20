package com.example.proyectofinal;

public class CrearEquipoDTO {
    private String nombreEquipo;
    private String categoria;
    private String deporte;
    private boolean tieneMultas;
    private String codigoGrupoEntrenador;
    private String codigoGrupoJugador;
    private String codigoGrupoFamiliar;
    private int idUsuarioCreador;

    public CrearEquipoDTO(String nombreEquipo, String categoria, String deporte, boolean tieneMultas,
                          String codigoGrupoEntrenador, String codigoGrupoJugador, String codigoGrupoFamiliar,
                          int idUsuarioCreador) {
        this.nombreEquipo = nombreEquipo;
        this.categoria = categoria;
        this.deporte = deporte;
        this.tieneMultas = tieneMultas;
        this.codigoGrupoEntrenador = codigoGrupoEntrenador;
        this.codigoGrupoJugador = codigoGrupoJugador;
        this.codigoGrupoFamiliar = codigoGrupoFamiliar;
        this.idUsuarioCreador = idUsuarioCreador;
    }

    // Getters y setters si los necesitas...


    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getCodigoGrupoJugador() {
        return codigoGrupoJugador;
    }

    public void setCodigoGrupoJugador(String codigoGrupoJugador) {
        this.codigoGrupoJugador = codigoGrupoJugador;
    }

    public boolean isTieneMultas() {
        return tieneMultas;
    }

    public void setTieneMultas(boolean tieneMultas) {
        this.tieneMultas = tieneMultas;
    }

    public String getCodigoGrupoEntrenador() {
        return codigoGrupoEntrenador;
    }

    public void setCodigoGrupoEntrenador(String codigoGrupoEntrenador) {
        this.codigoGrupoEntrenador = codigoGrupoEntrenador;
    }

    public String getCodigoGrupoFamiliar() {
        return codigoGrupoFamiliar;
    }

    public void setCodigoGrupoFamiliar(String codigoGrupoFamiliar) {
        this.codigoGrupoFamiliar = codigoGrupoFamiliar;
    }

    public int getIdUsuarioCreador() {
        return idUsuarioCreador;
    }

    public void setIdUsuarioCreador(int idUsuarioCreador) {
        this.idUsuarioCreador = idUsuarioCreador;
    }
}
