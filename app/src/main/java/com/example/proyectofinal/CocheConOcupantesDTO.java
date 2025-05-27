package com.example.proyectofinal;

import java.util.List;

public class CocheConOcupantesDTO {

    private int id;
    private String nombreConductor;
    private int numeroPlazas;
    private List<String> ocupantes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public int getNumeroPlazas() {
        return numeroPlazas;
    }

    public void setNumeroPlazas(int numeroPlazas) {
        this.numeroPlazas = numeroPlazas;
    }

    public List<String> getOcupantes() {
        return ocupantes;
    }

    public void setOcupantes(List<String> ocupantes) {
        this.ocupantes = ocupantes;
    }
}
