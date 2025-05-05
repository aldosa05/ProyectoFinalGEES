package com.example.proyectofinal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/usuarios/login")
    Call<Usuario> login(@Body Usuario usuario);  // Usamos Usuario como respuesta
}
