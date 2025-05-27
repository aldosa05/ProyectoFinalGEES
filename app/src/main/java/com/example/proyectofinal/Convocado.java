package com.example.proyectofinal;

public class Convocado {
    private int id;
    private int idPartido;
    private String nombreJugador;
    private int dorsal;

    // Getters y setters
    public int getId() { return id; }
    public int getIdPartido() { return idPartido; }
    public String getNombre() { return nombreJugador; }
    public int getDorsal() { return dorsal; }

    public void setId(int id) { this.id = id; }
    public void setIdPartido(int idPartido) { this.idPartido = idPartido; }
    public void setNombre(String nombre) { this.nombreJugador = nombreJugador; }
    public void setDorsal(int dorsal) { this.dorsal = dorsal; }
}

