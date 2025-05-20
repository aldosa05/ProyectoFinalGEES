package com.example.proyectofinal;

public class CambioContrasenaRequest {

    private String correo;
    private String nuevaContrasena;

    public CambioContrasenaRequest(String correo, String nuevaContrasena) {
        this.correo = correo;
        this.nuevaContrasena = nuevaContrasena;
    }
    public CambioContrasenaRequest(){

    }

    // Getters y setters
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getNuevaContrasena() { return nuevaContrasena; }
    public void setNuevaContrasena(String nuevaContrasena) { this.nuevaContrasena = nuevaContrasena; }


}
