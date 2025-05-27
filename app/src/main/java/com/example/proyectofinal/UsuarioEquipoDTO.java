package com.example.proyectofinal;

public class UsuarioEquipoDTO {

    private String rol;
    private int idEquipo;
    private String nombreEquipo;
    private String deporte;

    private boolean usaMultas;

    public boolean isUsaMultas() {
        return usaMultas;
    }

    public void setUsaMultas(boolean usaMultas) {
        this.usaMultas = usaMultas;
    }


    public String getRol() {
        return rol;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }
}
