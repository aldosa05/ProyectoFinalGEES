package com.example.proyectofinal;

import com.google.gson.annotations.SerializedName;

public class UsuarioEquipo {
    @SerializedName("id_usuario")  private int idUsuario;
    @SerializedName("id_equipo")   private int idEquipo;
    @SerializedName("rol")         private String rol;
    @SerializedName("equipo")      private Equipo equipo;   // anidado
    // getters y setters...

    public UsuarioEquipo() {
    }

    public UsuarioEquipo(int idUsuario, int idEquipo, String rol, Equipo equipo) {
        this.idUsuario = idUsuario;
        this.idEquipo = idEquipo;
        this.rol = rol;
        this.equipo = equipo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
