package com.example.proyectofinal;

public class ConvocadoRequest {
    private int idPartido;
    private String nombre;
    private int dorsal;

    public ConvocadoRequest(int idPartido, String nombre, int dorsal) {
        this.idPartido = idPartido;
        this.nombre = nombre;
        this.dorsal = dorsal;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDorsal() {
        return dorsal;
    }
}
