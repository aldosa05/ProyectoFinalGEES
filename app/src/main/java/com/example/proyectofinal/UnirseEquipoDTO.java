package com.example.proyectofinal;

public class UnirseEquipoDTO {

    private int idUsuario;
    private String codigoGrupo;

    public UnirseEquipoDTO(int idUsuario, String codigoGrupo) {
        this.idUsuario = idUsuario;
        this.codigoGrupo = codigoGrupo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

}
