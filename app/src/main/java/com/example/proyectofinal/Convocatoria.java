package com.example.proyectofinal;

import java.util.List;

public class Convocatoria {

    private int idPartido;
    private int idEquipo;
    private String equipoRival;
    private String fecha;         // se parsea como String en formato ISO: yyyy-MM-dd
    private String horaQuedada;   // formato HH:mm
    private String horaPartido;   // formato HH:mm
    private String lugar;

    // Getters y Setters

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getEquipoRival() {
        return equipoRival;
    }

    public void setEquipoRival(String equipoRival) {
        this.equipoRival = equipoRival;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraQuedada() {
        return horaQuedada;
    }

    public void setHoraQuedada(String horaQuedada) {
        this.horaQuedada = horaQuedada;
    }

    public String getHoraPartido() {
        return horaPartido;
    }

    public void setHoraPartido(String horaPartido) {
        this.horaPartido = horaPartido;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
