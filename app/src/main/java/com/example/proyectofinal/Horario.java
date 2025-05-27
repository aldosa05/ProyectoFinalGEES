package com.example.proyectofinal;

public class Horario {

    private int id;
    private String dia;
    private String hora_quedada;
    private String hora_entreno;
    private String lugar;
    private int id_equipo;

    public Horario(String dia, String hora_quedada, String hora_entreno, String lugar, int id_equipo) {
        this.dia = dia;
        this.hora_quedada = hora_quedada;
        this.hora_entreno = hora_entreno;
        this.lugar = lugar;
        this.id_equipo = id_equipo;
    }

    // Getters y Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora_quedada() {
        return hora_quedada;
    }

    public void setHora_quedada(String hora_quedada) {
        this.hora_quedada = hora_quedada;
    }

    public String getHora_entreno() {
        return hora_entreno;
    }

    public void setHora_entreno(String hora_entreno) {
        this.hora_entreno = hora_entreno;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getId_equipo() {
        return id_equipo;
    }

    public void setId_equipo(int id_equipo) {
        this.id_equipo = id_equipo;
    }

}
