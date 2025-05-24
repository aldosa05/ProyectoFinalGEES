package com.example.proyectofinal;

public class EquipoRolDeporteDTO {

    private int idEquipo;
    private String nombreEquipo;
    private String rol;
    private String deporte;

    public EquipoRolDeporteDTO() {
    }

    public EquipoRolDeporteDTO(int idEquipo, String nombreEquipo, String rol, String deporte) {
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.rol = rol;
        this.deporte = deporte;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }
}
